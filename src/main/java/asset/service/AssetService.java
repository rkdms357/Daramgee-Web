package asset.service;

import asset.dao.AssetDAO;
import asset.dto.AssetDTO;
import coin.dto.PriceDTO;
import coin.service.CoinService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssetService {

    private final AssetDAO assetDAO = new AssetDAO();
    private final CoinService coinService = new CoinService();

    // 전체 자산 조회 + 시세 최신화
    public List<AssetDTO> getAllAssets() {

        // 1. DB 조회
        List<AssetDTO> list = assetDAO.selectAll();
        if (list == null || list.isEmpty()) return list;

        // 2. symbol만 추출
        List<String> assetIds = list.stream()
                .map(AssetDTO::getAssetId)
                .collect(Collectors.toList());

        // 3. 빗썸 API 1회 호출
        Map<String, PriceDTO> priceMap = coinService.getPriceAndRate(assetIds);

        // 4. 변경된 자산만 수집
        List<AssetDTO> updateTargets = new ArrayList<>();

        for (AssetDTO asset : list) {
            PriceDTO info = priceMap.get(asset.getAssetId());
            if (info == null) continue;

            BigDecimal newPrice = info.getPrice();
            double newRate = info.getChangeRate();

            if (asset.getCurrentPrice() != null &&
                    asset.getCurrentPrice().compareTo(newPrice) == 0 &&
                    Double.compare(asset.getChangeRate(), newRate) == 0) {
                continue;
            }
            asset.setCurrentPrice(newPrice);
            asset.setChangeRate(newRate);
            updateTargets.add(asset);
        }
        // 5. DB Batch Update (1회)
        assetDAO.batchUpdatePriceAndRate(updateTargets);
        return list;
    }
}