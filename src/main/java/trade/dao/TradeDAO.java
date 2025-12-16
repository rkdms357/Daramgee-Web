package trade.dao;

import trade.dto.TradeDTO;
import util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeDAO {
    // 1. 매수 기능
    public int buy(String userId, String assetId, int quantity, BigDecimal price, BigDecimal totalCost) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = DBUtil.dbconnect();
            conn.setAutoCommit(false); //커밋하기전까지 DB반영안함
            // 1. 돈 빼기
            String sql = "update users set cash = cash - ? where user_id = ?";
            st = conn.prepareStatement(sql);
            st.setBigDecimal(1, totalCost);
            st.setString(2, userId);
            int r1 = st.executeUpdate();

            // 2. 지갑에 넣기
            // 이미 있는지 확인
            String checkSql = "select quantity, avg_price from portfolio where user_id = ? and asset_id = ?";
            st = conn.prepareStatement(checkSql);
            st.setString(1, userId);
            st.setString(2, assetId);
            rs = st.executeQuery();
            int r2 = 0;
            if (rs.next()) {
                // 2-1. 전에 구매했으면 -> 수량 늘리고 평단가 수정 (Update)
                int oldQty = rs.getInt("quantity");
                double oldAvg = rs.getDouble("avg_price");

                BigDecimal oldAvgDecimal = BigDecimal.valueOf(oldAvg);
                BigDecimal oldTotalCost = oldAvgDecimal.multiply(new BigDecimal(oldQty));

                BigDecimal newTotalCost = oldTotalCost.add(totalCost); // 이전 총 비용 + 이번 총 비용
                BigDecimal newQuantity = new BigDecimal(oldQty + quantity);
                BigDecimal newAvg = newTotalCost.divide(newQuantity, 8, BigDecimal.ROUND_HALF_UP); // 소수점 8자리 정밀도

                String updateSql = "update portfolio set quantity = quantity + ?, avg_price = ? where user_id = ? and asset_id = ?";
                st = conn.prepareStatement(updateSql);
                st.setInt(1, quantity);
                st.setBigDecimal(2, newAvg);
                st.setString(3, userId);
                st.setString(4, assetId);
                r2 = st.executeUpdate();
            } else {
                // 2-2. 전에 구매 안했으면 -> 새로 만들기 (Insert)
                String insertSql = "insert into portfolio (portfolio_id, quantity, avg_price, user_id, asset_id) " +
                        "values ((select nvl(max(portfolio_id),0)+1 from portfolio), ?, ?, ?, ?)";
                st = conn.prepareStatement(insertSql);
                st.setInt(1, quantity);
                st.setBigDecimal(2, price);
                st.setString(3, userId);
                st.setString(4, assetId);
                r2 = st.executeUpdate();
            }

            // 3. 거래 기록 남기기
            String logSql = "insert into trade (trade_id, trade_type, trade_quantity, trade_price, trade_date, user_id, asset_id) " +
                    "values ((select nvl(max(trade_id),0)+1 from trade), 'BUY', ?, ?, sysdate, ?, ?)";
            st = conn.prepareStatement(logSql);
            st.setInt(1, quantity);
            st.setBigDecimal(2, price);
            st.setString(3, userId);
            st.setString(4, assetId);
            int r3 = st.executeUpdate();
            if (r1 > 0 && r2 > 0 && r3 > 0) {
                conn.commit();
                result = 1;
            } else {
                conn.rollback();
                System.out.println("거래중 일부 단계가 실패하여 취소되었습니다.");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            } // 에러나면 롤백
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return result;
    }
    // 2. 매도 기능
    public int sell(String userId, String assetId, int quantity, BigDecimal price, BigDecimal totalCost) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = DBUtil.dbconnect();
            conn.setAutoCommit(false); //커밋하기전까지 DB반영안함
            // 1. 돈 넣기
            String sql = "update users set cash = cash + ? where user_id = ?";
            st = conn.prepareStatement(sql);
            st.setBigDecimal(1, totalCost);
            st.setString(2, userId);
            int r1 = st.executeUpdate();

            // 2. 지갑에 빼기
            // 이미 있는지 확인
            String checkSql = "select quantity from portfolio where user_id = ? and asset_id = ?";
            st = conn.prepareStatement(checkSql);
            st.setString(1, userId);
            st.setString(2, assetId);
            rs = st.executeQuery();
            int r2 = 0;
            if (rs.next()) {
                int currentQty = rs.getInt("quantity");
                if (currentQty > quantity) {
                    // 2-1. 일부만 파는 경우 -> 수량만 줄임 (평단가는 안 바뀜)
                    String updateSql = "update portfolio set quantity = quantity - ? where user_id = ? and asset_id = ?";
                    st = conn.prepareStatement(updateSql);
                    st.setInt(1, quantity);
                    st.setString(2, userId);
                    st.setString(3, assetId);
                    r2 = st.executeUpdate();
                } else {
                    // 2-2. 싹 다 파는 경우 (또는 그 이상) -> 지갑에서 아예 삭제
                    String deleteSql = "delete from portfolio where user_id = ? and asset_id = ?";
                    st = conn.prepareStatement(deleteSql);
                    st.setString(1, userId);
                    st.setString(2, assetId);
                    r2 = st.executeUpdate();
                }
            }
            // 3. 거래 기록 남기기
            String logSql = "insert into trade (trade_id, trade_type, trade_quantity, trade_price, trade_date, user_id, asset_id) " +
                    "values ((select nvl(max(trade_id),0)+1 from trade), 'SELL', ?, ?, sysdate, ?, ?)";
            st = conn.prepareStatement(logSql);
            st.setInt(1, quantity);
            st.setBigDecimal(2, price);
            st.setString(3, userId);
            st.setString(4, assetId);
            int r3 = st.executeUpdate();
            if (r1 > 0 && r2 > 0 && r3 > 0) {
                conn.commit();
                result = 1;
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return result;
    }

    // 3. 보유 코인 개수 확인
    public int getQuantity(String userId, String assetId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int result = 0;
        String sql = "select quantity from portfolio where user_id = ? and asset_id = ?";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, userId);
            st.setString(2, assetId);
            rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return result;
    }

    // 4-1. 거래 내역 전체 조회
    public List<TradeDTO> selectAll(String userId, int page) {
        List<TradeDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        int pageSize = 15;
        // 시작 번호와 끝 번호 계산 (1페이지: 1~15, 2페이지: 16~30)
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        String sql = "select * from (" +
                "  select a.*, ROWNUM rnum from (" +
                "    select * from trade where user_id = ? order by trade_date desc" +
                "  ) a" +
                ") where rnum between ? and ?";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, userId);
            st.setInt(2, startRow);
            st.setInt(3, endRow);
            rs = st.executeQuery();
            while (rs.next()) {
                TradeDTO t = new TradeDTO();
                t.setTradeId(rs.getInt("trade_id"));
                t.setTradeType(rs.getString("trade_type")); // BUY, SELL
                t.setAssetId(rs.getString("asset_id"));     // KRW-BTC
                t.setTradeQuantity(rs.getInt("trade_quantity"));
                t.setTradePrice(rs.getBigDecimal("trade_price"));
                t.setTradeDate(rs.getDate("trade_date"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return list;
    }

    // 4-2. 전체 거래 기록 개수 조회 (총 페이지 수 계산용)
    public int getTotalCount(String userId) {
        int count = 0;
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "select count(*) from trade where user_id = ?";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, userId);
            rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return count;
    }
}