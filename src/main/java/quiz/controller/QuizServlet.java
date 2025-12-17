package quiz.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.dto.MemberDTO;
import quiz.dto.QuizDTO;
import quiz.service.QuizService;
import java.io.IOException;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private final QuizService quizService = new QuizService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (session != null) ? (MemberDTO) session.getAttribute("loginUser") : null;

        // 로그인 체크
        if (loginUser == null) {
            session.setAttribute("msg", "로그인이 필요한 서비스입니다. 퀴즈를 맞히고 10만원을 받으세요!");
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        String userId = loginUser.getUserId();

        // 2. 중복 참여 체크
        if (!quizService.canSolveQuiz(userId)) {
            request.setAttribute("msg", "오늘은 이미 참여하셨습니다. 내일 또 오세요! 🐿️");
            request.setAttribute("isSolved", true); // JSP에서 버튼 등을 비활성화할 용도
        } else {
            // 3. 문제 출제
            QuizDTO quiz = quizService.getQuiz();
            if (quiz == null) {
                request.setAttribute("msg", "준비된 퀴즈가 없습니다.");
            } else {
                request.setAttribute("quiz", quiz);
            }
        }

        // 결과 메시지(POST 후 전달된 메시지) 처리
        String quizResult = (String) session.getAttribute("quizResult");
        if (quizResult != null) {
            request.setAttribute("quizResult", quizResult);
            session.removeAttribute("quizResult");
        }

        request.getRequestDispatcher("/WEB-INF/views/quiz/quiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        int quizId = Integer.parseInt(request.getParameter("quizId"));
        String userAnswer = request.getParameter("userAnswer");
        String realAnswer = request.getParameter("realAnswer"); // 혹은 서비스에서 다시 조회

        // 정답 제출 및 보상 지급
        String resultMsg = quizService.submitAnswer(loginUser.getUserId(), quizId, userAnswer, realAnswer);

        // [PRG 패턴] 결과 메시지를 세션에 담고 다시 GET으로 리다이렉트
        session.setAttribute("quizResult", resultMsg);
        response.sendRedirect(request.getContextPath() + "/quiz");
    }
}