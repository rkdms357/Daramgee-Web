package trade.controller;

import asset.dto.AssetDTO;
import asset.service.AssetService;
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

@WebServlet("/trade/buy")
public class TradeBuyServlet extends HttpServlet {

    private final TradeService tradeService = new TradeService();
    private final AssetService assetService = new AssetService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO)session.getAttribute("loginUser");

        // 1. 코인 시세 정보 및 사용자 현금 조회
        List<AssetDTO> assetList = assetService.getAllAssets();

        request.setAttribute("assets", assetList);
        request.setAttribute("cash", loginUser.getCash());

        // 거래 실행 후 세션에 저장된 메시지가 있다면 가져와서 삭제 (PRG 패턴 후처리)
        String msg = (String)session.getAttribute("tradeMsg");
        if (msg != null) {
            request.setAttribute("msg", msg);
            session.removeAttribute("tradeMsg");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/trade/buy.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (MemberDTO)session.getAttribute("loginUser");

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
            String msg = tradeService.buyCoin(symbol, count, loginUser);

            // 결과 메시지를 세션에 저장 후 매수 페이지로 리다이렉트 (PRG 패턴)
            session.setAttribute("tradeMsg", msg);
            response.sendRedirect(request.getContextPath() + "/trade/buy");
        } catch (NumberFormatException e) {
            session.setAttribute("tradeMsg", "매수 수량은 1 이상이어야 합니다.");
            response.sendRedirect(request.getContextPath() + "/trade/buy");
        } catch (Exception e) {
            session.setAttribute("tradeMsg", "거래 중 알 수 없는 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/trade/buy");
        }
    }
}