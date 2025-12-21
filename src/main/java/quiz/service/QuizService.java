package quiz.service;

import member.service.MemberService;
import quiz.dao.QuizDAO;
import quiz.dto.QuizDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class QuizService {
    QuizDAO quizDAO = new QuizDAO();
    MemberService memberService = new MemberService();

    // 오늘 풀었는지 체크
    public boolean canSolveQuiz(String userId) {
        try (Connection conn = DBUtil.dbconnect()) {
            return !quizDAO.hasSolvedToday(conn, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 문제 가져오기
    public QuizDTO getQuiz() {
        try (Connection conn = DBUtil.dbconnect()) {
            return quizDAO.getRandomQuiz(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 정답 제출 및 결과 처리
    public String submitAnswer(String userId, int quizId,
                               String userAnswer, String realAnswer) {
        String msg = "";
        try (Connection conn = DBUtil.dbconnect()) {
            conn.setAutoCommit(false); // 트랜잭션 시작
            if (userAnswer.equals(realAnswer)) {
                memberService.updateCashService(conn, userId, 100000);
                quizDAO.insertLog(conn, userId, quizId, "Y");
                conn.commit();
                msg = "정답입니다! 투자 지원금 100,000원이 입금되었습니다.";
            } else {
                quizDAO.insertLog(conn, userId, quizId, "N");
                conn.commit();
                msg = "땡! 오답입니다. (정답: " + realAnswer + "번) 내일 다시 도전하세요!";
            }
        } catch (Exception e) {
            msg = "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        } return msg;
    }
}