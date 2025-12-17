package portfolio.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.dto.MemberDTO;
import portfolio.service.PortfolioViewService;
import java.io.IOException;

@WebServlet("/member/mypage")
public class PortfolioServlet extends HttpServlet {
    private final PortfolioViewService portfolioViewService = new PortfolioViewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        String userId = loginUser.getUserId();

        // 공통 호출
        portfolioViewService.prepareMyPortfolio(request, userId);
        request.setAttribute("member", loginUser);
        request.getRequestDispatcher("/member/mypage.jsp").forward(request, response);
    }
}