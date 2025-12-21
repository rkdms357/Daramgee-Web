package quiz.dao;

import quiz.dto.QuizDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizDAO {
    // 1. 랜덤 퀴즈 1개 가져오기
    public QuizDTO getRandomQuiz(Connection conn) throws SQLException {
        QuizDTO quiz = null;
        String sql = "select quiz_id, question, answer, choice from (select * from QUIZ order by dbms_random.value) where rownum <= 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    quiz = new QuizDTO();
                    quiz.setQuizId(rs.getInt("quiz_id"));
                    quiz.setQuestion(rs.getString("question"));
                    quiz.setAnswer(rs.getString("answer"));

                    // DB에 있는 "매수/매도/.." 문자열을 잘라서 배열로 만듦
                    String choices = rs.getString("choice");
                    if (choices != null) quiz.setOptions(choices.split("/"));
                }
            }
        }
        return quiz;
    }

    // 2. 오늘 풀었는지 확인 (중복 방지)
    public boolean hasSolvedToday(Connection conn, String userId) throws SQLException {
        String sql = "select count(*) from QUIZ_LOG where user_id = ? and TRUNC(solve_date) = TRUNC(SYSDATE)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // 3. 결과 기록 (로그 저장)
    public void insertLog(Connection conn, String userId, int quizId, String correctYn) throws SQLException {
        String sql = "insert into QUIZ_LOG (user_id, quiz_id, solve_date, correct_yn) "
                + "values (?, ?, SYSDATE, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userId);
            st.setInt(2, quizId);
            st.setString(3, correctYn);
            st.executeUpdate();
        }
    }
}