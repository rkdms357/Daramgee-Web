package trade.controller;

import trade.dto.TradeDTO;
import trade.service.TradeService;
import member.dto.MemberDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/trade/history")
public class TradeHistoryServlet extends HttpServlet {

    private final TradeService tradeService = new TradeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        String userId = ((MemberDTO) session.getAttribute("loginUser")).getUserId();

        // 거래 내역 조회
        List<TradeDTO> historyList = tradeService.getTradeHistory(userId);

        request.setAttribute("historyList", historyList);

        RequestDispatcher rd = request.getRequestDispatcher("/trade/history.jsp");
        rd.forward(request, response);
    }
}