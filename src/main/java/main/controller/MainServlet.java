package main.controller;

import asset.service.AssetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.dto.MemberDTO;
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
        MemberDTO loginUser = (session != null) ? (MemberDTO) session.getAttribute("loginUser") : null;

        boolean canSolve = true;
        boolean isSolved = false;

        if (loginUser != null) {
            canSolve = quizService.canSolveQuiz(loginUser.getUserId());
            isSolved = !canSolve;
        }

        request.setAttribute("quiz", quizService.getQuiz());
        request.setAttribute("canSolve", canSolve);
        request.setAttribute("isSolved", isSolved);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}

