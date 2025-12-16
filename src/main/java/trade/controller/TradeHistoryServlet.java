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

        // 1. 현재 페이지 번호 파악 (기본값 1)
        int currentPage = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // 2. 페이징된 데이터 조회
        List<TradeDTO> historyList = tradeService.getTradeHistoryPaging(userId, currentPage);

        // 3. 전체 페이지 수 계산
        int totalCount = tradeService.getTotalTradeCount(userId);
        int pageSize = 15;
        // 올림 처리를 통해 총 페이지 수 계산 (예: 16개면 2페이지)
        int totalPages = (int)Math.ceil((double)totalCount/pageSize);

        // 4. JSP에 정보 전달
        request.setAttribute("historyList", historyList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        RequestDispatcher rd = request.getRequestDispatcher("/trade/history.jsp");
        rd.forward(request, response);
    }
}