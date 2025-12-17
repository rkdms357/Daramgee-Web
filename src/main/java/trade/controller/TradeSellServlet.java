package trade.controller;

import portfolio.service.PortfolioViewService;
import trade.service.TradeService;
import member.dto.MemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/trade/sell")
public class TradeSellServlet extends HttpServlet {

    private final TradeService tradeService = new TradeService();
    private final PortfolioViewService portfolioViewService = new PortfolioViewService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        String userId = loginUser.getUserId();

        // 공통 서비스 호출
        portfolioViewService.prepareMyPortfolio(request, userId);

        String msg = (String) session.getAttribute("tradeMsg");
        if (msg != null) {
            request.setAttribute("msg", msg);
            session.removeAttribute("tradeMsg");
        }

        request.getRequestDispatcher("/trade/sell.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }
        String symbol = request.getParameter("symbol");
        String countStr = request.getParameter("count");

        try {
            int count = Integer.parseInt(countStr);
            if (count <= 0) throw new NumberFormatException();

            // 거래 실행 및 메시지 받기
            String msg = tradeService.sellCoin(symbol, count, loginUser);

            // 결과 메시지를 세션에 저장 후 매도 페이지로 리다이렉트
            session.setAttribute("tradeMsg", msg);
            response.sendRedirect(request.getContextPath() + "/trade/sell");

        } catch (NumberFormatException e) {
            session.setAttribute("tradeMsg", "매도 개수는 유효한 양의 정수여야 합니다.");
            response.sendRedirect(request.getContextPath() + "/trade/sell");
        } catch (Exception e) {
            session.setAttribute("tradeMsg", "거래 중 알 수 없는 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/trade/sell");
        }
    }
}