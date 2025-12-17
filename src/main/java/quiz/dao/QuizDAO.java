package quiz.dao;

import quiz.dto.QuizDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizDAO {
    // 1. 랜덤 퀴즈 1개 가져오기
    public QuizDTO getRandomQuiz() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs =  null;
        QuizDTO quiz = null;
        String sql = "select * from (select * from QUIZ order by dbms_random.value) where rownum <= 1";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                quiz = new QuizDTO();
                quiz.setQuizId(rs.getInt("quiz_id"));
                quiz.setQuestion(rs.getString("question"));
                quiz.setAnswer(rs.getString("answer"));
                // ★ 핵심: DB에 있는 "매수/매도/.." 문자열을 잘라서 배열로 만듦
                String choices = rs.getString("choice");
                if(choices != null) {
                    quiz.setOptions(choices.split("/"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return quiz;
    }

    // 2. 오늘 풀었는지 확인 (중복 방지)
    public boolean hasSolvedToday(String userId) {
        Connection conn = null;
        PreparedStatement st =  null;
        ResultSet rs =   null;
        boolean result = false;
        String sql = "select count(*) from QUIZ_LOG where user_id = ? and TRUNC(solve_date) = TRUNC(SYSDATE)";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, userId);
            rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return result;
    }

    // 3. 결과 기록 (로그 저장)
    public void insertLog(String userId, int quizId, String correctYn) {
        Connection conn = null;
        PreparedStatement st =  null;
        ResultSet rs =   null;
        String sql = "insert into QUIZ_LOG (quiz_log_id, solve_date, correct_yn, user_id, quiz_id) " +
                "values ((select NVL(MAX(quiz_log_id),0)+1 from QUIZ_LOG), SYSDATE, ?, ?, ?)";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setString(1, correctYn);
            st.setString(2, userId);
            st.setInt(3, quizId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, null);
        }
    }

    // 4. 보상 지급 (현금 Update)
    public int giveReward(String userId, int amount) {
        Connection conn =  null;
        PreparedStatement st =   null;
        String sql = "update USERS set cash = cash + ? where user_id = ?";
        int result = 0;
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            st.setInt(1, amount);
            st.setString(2, userId);
            result = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, null);
        }
        return result;
    }
}