package portfolio.service;

import asset.dto.AssetDTO;
import asset.service.AssetService;
import coin.service.CoinService;
import portfolio.dao.PortfolioDAO;
import portfolio.dto.PortfolioDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PortfolioService {
    PortfolioDAO portfolioDAO = new PortfolioDAO();
    CoinService coinService = new CoinService();
    AssetService assetService = new AssetService();

    public List<PortfolioDTO> getMyPortfolio(String userId) {
        // 1. DB에서 코인 목록 가져오기
        List<PortfolioDTO> list = portfolioDAO.selectAll(userId);

        Map<String, AssetDTO> assetMap = assetService.getAllAssets()
                .stream()
                .collect(Collectors.toMap(
                        AssetDTO::getAssetId,
                        a -> a
                ));

        // 2. 계산 (시세 조회 -> 수익률 계산)
        for (PortfolioDTO dto : list) {
            // 코인명 세팅
            AssetDTO asset = assetMap.get(dto.getAssetId());
            if (asset != null) {
                dto.setName(asset.getName());
            }

            // 빗썸 현재가 조회
            BigDecimal currentPrice = coinService.getPrice(dto.getAssetId());
            BigDecimal quantity = new BigDecimal(dto.getQuantity());
            BigDecimal avgPrice = dto.getAvgPrice();

            // 평가 금액 = 현재가 * 수량
            BigDecimal totalValue = currentPrice.multiply(quantity);
            // 투자 원금 = 평단가 * 수량
            BigDecimal buyValue = avgPrice.multiply(quantity);
            // 손익 = 평가금액 - 투자원금
            BigDecimal profit = totalValue.subtract(buyValue);

            double yield = 0;
            if (buyValue.compareTo(BigDecimal.ZERO) > 0) {
                // (수익 / 투자원금) * 100
                yield = profit.divide(buyValue, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100))
                        .doubleValue();
            }
            dto.setCurrentPrice(currentPrice);
            dto.setTotalValue(totalValue);
            dto.setProfit(profit);
            dto.setYield(yield);
        }
        return list;
    }
}