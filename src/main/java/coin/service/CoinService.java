package coin.service;

import coin.dto.PriceDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class BithumbResponse {
    String status;
    JsonElement data;
}

public class CoinService {

    private static final String BITHUMB_URL_ALL = "https://api.bithumb.com/public/ticker/ALL_KRW";
    private final Gson gson = new Gson();

    public String extractSymbol(String assetId) {
        if (assetId == null || !assetId.contains("-")) {
            throw new IllegalArgumentException("잘못된 assetId: " + assetId);
        }
        return assetId.split("-", 2)[1];
    }

    public Map<String, PriceDTO> getPriceAndRate(List<String> assetIds) {

        Map<String, PriceDTO> resultMap = new HashMap<>();

        try {
            URL url = new URL(BITHUMB_URL_ALL);
            // 1. 응답 전체 문자열로 읽기
            String responseBody;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                responseBody = br.lines().collect(Collectors.joining());
            }

            // 2. 문자열 오류 응답 방지 ("요청 한도 초과")
            if (responseBody.startsWith("\"")) {
                System.err.println("빗썸 API 오류 응답: " + responseBody);
                return resultMap;
            }

            // 3. JSON 파싱
            BithumbResponse response = gson.fromJson(responseBody, BithumbResponse.class);

            if (response == null || !"0000".equals(response.status)) {
                System.err.println("빗썸 API 오류");
                return resultMap;
            }

            JsonObject dataObject = response.data.getAsJsonObject();

            // 4. 필요한 코인만 추출
            for (String assetId : assetIds) {
                String symbol = extractSymbol(assetId);
                if (!dataObject.has(symbol)) continue;
                JsonObject coinObj = dataObject.getAsJsonObject(symbol);
                BigDecimal price = coinObj.get("closing_price").getAsBigDecimal();
                double rate = coinObj.get("fluctate_rate_24H").getAsDouble();
                resultMap.put(assetId, new PriceDTO(price, rate));
            }
        } catch (Exception e) {
            System.err.println("전체 시세 조회 실패");
            e.printStackTrace();
        }
        return resultMap;
    }

    public BigDecimal getPrice(String assetId) {
        Map<String, PriceDTO> map = getPriceAndRate(List.of(assetId));

        if (!map.containsKey(assetId)) {
            System.err.println(assetId + " 시세 정보를 찾을 수 없습니다.");
            return BigDecimal.ZERO;
        }

        return map.get(assetId).getPrice();
    }
}