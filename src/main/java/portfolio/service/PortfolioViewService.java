package portfolio.service;

import jakarta.servlet.http.HttpServletRequest;
import portfolio.dto.PortfolioDTO;
import java.util.List;

public class PortfolioViewService {
    private final PortfolioService portfolioService = new PortfolioService();

    public void prepareMyPortfolio(HttpServletRequest request, String userId) {
        List<PortfolioDTO> myList = portfolioService.getMyPortfolio(userId);
        request.setAttribute("myList", myList);
        System.out.println(myList);
    }
}
