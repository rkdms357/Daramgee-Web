package member.service;

import member.dao.MemberDAO;
import member.dto.MemberDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class MemberService {

    MemberDAO memberDAO = new MemberDAO();

    public MemberDTO selectById(String userId) {
        return memberDAO.selectById(userId);
    }

    public String deleteService(String userId) {
        Connection conn = null;
        try {
            conn = DBUtil.dbconnect();
            conn.setAutoCommit(false); // 트랜잭션 시작

            memberDAO.deleteTrades(conn, userId);
            memberDAO.deletePortfolio(conn, userId);
            memberDAO.deleteQuizLog(conn, userId);

            int result = memberDAO.deleteUser(conn, userId);
            if (result == 0) {
                conn.rollback();
                return "삭제할 회원 정보가 없습니다.";
            }

            conn.commit();
            return "탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다. 🐿️";

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return "회원 탈퇴 중 오류가 발생했습니다.";
        } finally {
            DBUtil.dbDisconnect(conn, null, null);
        }
    }

    public String insertService(MemberDTO member) {
        return memberDAO.insert(member);
    }

    public int updateCashService(Connection conn, String userId, int amount) throws SQLException {
        return memberDAO.updateCash(conn, userId, amount);
    }
}
