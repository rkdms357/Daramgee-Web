package main.controller;

import asset.service.AssetService;
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

@WebServlet("")
public class MainServlet extends HttpServlet {

    private final AssetService assetService = new AssetService();
    private final QuizService quizService = new QuizService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        MemberDTO loginUser =
                (session != null) ? (MemberDTO) session.getAttribute("loginUser") : null;

        if (loginUser != null) {
            request.setAttribute("assets", assetService.getAllAssets());

            if (!quizService.canSolveQuiz(loginUser.getUserId())) {
                request.setAttribute("msg", "오늘은 이미 참여하셨습니다. 내일 또 오세요! 🐿️");
                request.setAttribute("isSolved", true);
            } else {
                QuizDTO quiz = quizService.getQuiz();
                request.setAttribute("quiz", quiz);
            }

            request.getRequestDispatcher("/main/main.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}

