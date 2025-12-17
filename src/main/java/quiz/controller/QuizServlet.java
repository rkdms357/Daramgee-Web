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
import java.math.BigDecimal;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private final QuizService quizService = new QuizService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (session != null) ? (MemberDTO) session.getAttribute("loginUser") : null;

        // 로그인 여부만 JSP에 전달
        boolean needLogin = (loginUser == null);
        request.setAttribute("needLogin", needLogin);

        // 기본값 (비로그인 or 아직 안 품)
        boolean canSolve = true;
        boolean isSolved = false;

        // 로그인한 경우만 중복 체크
        if (!needLogin) {
            canSolve = quizService.canSolveQuiz(loginUser.getUserId());
            isSolved = !canSolve;
        }

        request.setAttribute("canSolve", canSolve);
        request.setAttribute("isSolved", isSolved);

        // 아직 풀 수 있을 때만 문제 내려줌
        if (canSolve) {
            QuizDTO quiz = quizService.getQuiz();
            request.setAttribute("quiz", quiz);
        }

        request.getRequestDispatcher("/WEB-INF/views/quiz/quiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("msg", "로그인이 필요한 서비스입니다. 퀴즈를 맞히고 10만원을 받으세요!");
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        int quizId = Integer.parseInt(request.getParameter("quizId"));
        String userAnswer = request.getParameter("userAnswer");
        String realAnswer = request.getParameter("realAnswer"); // 혹은 서비스에서 다시 조회

        // 정답 제출 및 보상 지급
        String resultMsg = quizService.submitAnswer(loginUser.getUserId(), quizId, userAnswer, realAnswer);

        // 정답이면 세션 cash 값도 갱신
        if (userAnswer.equals(realAnswer)) {
            BigDecimal reward = new BigDecimal("100000");     // 10만원
            loginUser.setCash(loginUser.getCash().add(reward));   // 기존 cash + 100000
            session.setAttribute("loginUser", loginUser);
            session.setAttribute("loginUser", loginUser);   // 세션 갱신
        }

        // [PRG 패턴] 결과 메시지를 세션에 담고 다시 GET으로 리다이렉트
        session.setAttribute("quizResult", resultMsg);
        response.sendRedirect(request.getContextPath() + "/");
    }
}