package trade.dao;

import trade.dto.TradeDTO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TradeDAO {
    // 1. 매수 기능
    public int buy(Connection conn, String userId, String assetId, int quantity,
                   BigDecimal price, BigDecimal totalCost) throws SQLException {
        int r1 = 0, r2 = 0, r3 = 0;

        // 1. 돈 빼기
        String sql = "update users set cash = cash - ? where user_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setBigDecimal(1, totalCost);
            st.setString(2, userId);
            r1 = st.executeUpdate();
        }

        // 2. 지갑에 넣기
        String checkSql = "select quantity, avg_price from portfolio where user_id = ? and asset_id = ?";
        try (PreparedStatement st = conn.prepareStatement(checkSql)) {
            st.setString(1, userId);
            st.setString(2, assetId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int oldQty = rs.getInt("quantity");
                    BigDecimal oldAvg = rs.getBigDecimal("avg_price");
                    BigDecimal newTotalCost = oldAvg.multiply(BigDecimal.valueOf(oldQty)).add(totalCost);
                    BigDecimal newQuantity = BigDecimal.valueOf(oldQty + quantity);
                    BigDecimal newAvg = newTotalCost.divide(newQuantity, 8, BigDecimal.ROUND_HALF_UP);

                    String updateSql = "update portfolio set quantity = quantity + ?, avg_price = ? where user_id = ? and asset_id = ?";
                    try (PreparedStatement st2 = conn.prepareStatement(updateSql)) {
                        st2.setInt(1, quantity);
                        st2.setBigDecimal(2, newAvg);
                        st2.setString(3, userId);
                        st2.setString(4, assetId);
                        r2 = st2.executeUpdate();
                    }
                } else {
                    String insertSql = "insert into portfolio (user_id, asset_id, quantity, avg_price) values (?, ?, ?, ?)";
                    try (PreparedStatement st2 = conn.prepareStatement(insertSql)) {
                        st2.setString(1, userId);
                        st2.setString(2, assetId);
                        st2.setInt(3, quantity);
                        st2.setBigDecimal(4, price);
                        r2 = st2.executeUpdate();
                    }
                }
            }
        }

        // 3. 거래 기록 남기기
        String logSql = "insert into trade (user_id, asset_id, trade_type, trade_quantity, trade_price, trade_date) "
                + "values (?, ?, 'BUY', ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(logSql)) {
            st.setString(1, userId);
            st.setString(2, assetId);
            st.setInt(3, quantity);
            st.setBigDecimal(4, price);
            // KST 기준 timestamp 생성
            Timestamp kstTime = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            st.setTimestamp(5, kstTime);
            r3 = st.executeUpdate();
        }
        return (r1 > 0 && r2 > 0 && r3 > 0) ? 1 : 0;
    }

    // 2. 매도 기능
    public int sell(Connection conn, String userId, String assetId, int quantity,
                    BigDecimal price, BigDecimal totalCost) throws SQLException {
        int r1 = 0, r2 = 0, r3 = 0;

        // 돈 넣기
        String sql = "update users set cash = cash + ? where user_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setBigDecimal(1, totalCost);
            st.setString(2, userId);
            r1 = st.executeUpdate();
        }

        // 지갑에서 빼기
        String checkSql = "select quantity from portfolio where user_id = ? and asset_id = ?";
        try (PreparedStatement st = conn.prepareStatement(checkSql)) {
            st.setString(1, userId);
            st.setString(2, assetId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int currentQty = rs.getInt("quantity");
                    if (currentQty > quantity) {
                        String updateSql = "update portfolio set quantity = quantity - ? where user_id = ? and asset_id = ?";
                        try (PreparedStatement st2 = conn.prepareStatement(updateSql)) {
                            st2.setInt(1, quantity);
                            st2.setString(2, userId);
                            st2.setString(3, assetId);
                            r2 = st2.executeUpdate();
                        }
                    } else {
                        String deleteSql = "delete from portfolio where user_id = ? and asset_id = ?";
                        try (PreparedStatement st2 = conn.prepareStatement(deleteSql)) {
                            st2.setString(1, userId);
                            st2.setString(2, assetId);
                            r2 = st2.executeUpdate();
                        }
                    }
                }
            }
        }

        // 거래 기록
        String logSql = "insert into trade (user_id, asset_id, trade_type, trade_quantity, trade_price, trade_date) "
                + "values (?, ?, 'SELL', ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(logSql)) {
            st.setString(1, userId);
            st.setString(2, assetId);
            st.setInt(3, quantity);
            st.setBigDecimal(4, price);
            // KST 기준 timestamp 생성
            Timestamp kstTime = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            st.setTimestamp(5, kstTime);
            r3 = st.executeUpdate();
        }
        return (r1 > 0 && r2 > 0 && r3 > 0) ? 1 : 0;
    }

    // 3. 보유 코인 개수 확인
    public int getQuantity(Connection conn, String userId, String assetId) throws SQLException {
        int result = 0;
        String sql = "select quantity from portfolio where user_id = ? and asset_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userId);
            st.setString(2, assetId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) result = rs.getInt("quantity");
            }
        }
        return result;
    }

    // 4. 거래 내역 전체 조회
    public List<TradeDTO> selectAll(Connection conn, String userId, int page) throws SQLException {
        List<TradeDTO> list = new ArrayList<>();
        int pageSize = 15;
        // 시작 번호와 끝 번호 계산 (1페이지: 1~15, 2페이지: 16~30)
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        String sql = "select * from (" +
                "  select a.*, ROWNUM rnum from (" +
                "    select * from trade where user_id = ? order by trade_date desc" +
                "  ) a" +
                ") where rnum between ? and ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userId);
            st.setInt(2, startRow);
            st.setInt(3, endRow);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    TradeDTO t = new TradeDTO();
                    t.setTradeType(rs.getString("trade_type")); // BUY, SELL
                    t.setAssetId(rs.getString("asset_id"));     // KRW-BTC
                    t.setTradeQuantity(rs.getInt("trade_quantity"));
                    t.setTradePrice(rs.getBigDecimal("trade_price"));
                    t.setTradeDate(rs.getTimestamp("trade_date"));
                    list.add(t);
                }
            }
            return list;
        }
    }

    // 5. 전체 거래 기록 개수 조회 (총 페이지 수 계산용)
    public int getTotalCount(Connection conn, String userId) throws SQLException {
        int count = 0;
        String sql = "select count(*) from trade where user_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        }
        return count;
    }
}