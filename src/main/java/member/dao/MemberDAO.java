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
        String sql = "select * from users where user_id = ?";
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
            //st.setInt(3, member.getPoints());
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
    public String delete(String userId) {
        String msg = null;
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = DBUtil.dbconnect();
            conn.setAutoCommit(false); //트랜잭션 시작

            // 자식 데이터 삭제
            String sql1 = "delete from trade where user_id = ?";
            st = conn.prepareStatement(sql1);
            st.setString(1, userId);
            st.executeUpdate();
            st.close();

            // 자식 데이터 삭제 (보유 코인)
            String sql2 = "delete from portfolio where user_id = ?";
            st = conn.prepareStatement(sql2);
            st.setString(1, userId);
            st.executeUpdate();
            st.close();

            // 자식 데이터 삭제 (퀴즈 기록)
            String sql3 = "delete from quiz_log where user_id = ?";
            st = conn.prepareStatement(sql3);
            st.setString(1, userId);
            st.executeUpdate();
            st.close();

            // 부모(회원) 삭제
            String sql4 = "delete from users where user_id = ?";
            st = conn.prepareStatement(sql4);
            st.setString(1, userId);
            int result = st.executeUpdate();

            if(result > 0) {
                conn.commit(); // 모든 삭제 확정
                msg = "탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다. 🐿️";
            } else {
                conn.rollback(); // 실패하면 되돌리기
                msg = "삭제할 회원 정보가 없습니다.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, null);
        }
        return msg;
    }
}