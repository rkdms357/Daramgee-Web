package member.dao;

import member.dto.MemberDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {
    // 1. 내 정보 조회 (로그인 겸용)
    public MemberDTO selectById(String userId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "select user_id, password, cash from users where user_id = ?";
        MemberDTO member = null;
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, userId);
            rs = st.executeQuery();
            while (rs.next()) {
                member = new MemberDTO();
                member.setUserId(rs.getString("user_id"));
                member.setPassword(rs.getString("password"));
                member.setCash(rs.getBigDecimal("cash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return member;
    }

    // 2. 회원 가입
    public String insert(MemberDTO member) {
        String msg = null;
        Connection conn = null;
        PreparedStatement st = null;
        String sql = "insert into users values (?, ?, ?)";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, member.getUserId());
            st.setString(2, member.getPassword());
            st.setBigDecimal(3, member.getCash());
            int result = st.executeUpdate();
            if (result > 0) {
                msg = "회원가입 되었습니다.";
            } else {
                msg = "회원가입이 실패하였습니다."; // DB반영 안됨
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, null);
        }
        return msg;
    }

    // 3. 회원 탈퇴
    public void deleteTrades(Connection conn, String userId) throws SQLException {
        String sql = "delete from trade where user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }

    public void deletePortfolio(Connection conn, String userId) throws SQLException {
        String sql = "delete from portfolio where user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }

    public void deleteQuizLog(Connection conn, String userId) throws SQLException {
        String sql = "delete from quiz_log where user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }

    public int deleteUser(Connection conn, String userId) throws SQLException {
        String sql = "delete from users where user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            return ps.executeUpdate();
        }
    }

    // 4. 캐쉬 업데이트
    public int updateCash(Connection conn, String userId, int amount) throws SQLException {
        String sql = "update users set cash = cash + ? where user_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, amount);
            st.setString(2, userId);
            return st.executeUpdate();
        }
    }
}