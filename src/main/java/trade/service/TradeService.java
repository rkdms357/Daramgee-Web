package trade.service;

import coin.service.CoinService;
import member.dto.MemberDTO;
import trade.dao.TradeDAO;
import trade.dto.TradeDTO;

import java.math.BigDecimal;
import java.util.List;

public class TradeService {
    TradeDAO tradeDAO = new TradeDAO();
    CoinService coinService = new CoinService();

    // 매수 로직
    public String buyCoin(String symbol, int count, MemberDTO member) {
        // 1. 코인 코드 만들기 (BTC -> KRW-BTC)
        String assetId = "KRW-" + symbol.toUpperCase();
        String userId = member.getUserId();

        // 2. 현재 가격 조회 (빗썸 API)
        BigDecimal currentPrice = coinService.getPrice(assetId);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) return "시세 조회를 실패했습니다.";

        // 3. 총비용 계산 (int를 BigDecimal로 변환하여 곱셈)
        BigDecimal countDecimal = new BigDecimal(count);
        BigDecimal totalCost = currentPrice.multiply(countDecimal);

        // 사용자 현금 잔액 (MemberDTO에서 가져오고, BigDecimal로 변환)
        BigDecimal myCash = member.getCash();

        // 4. 잔액 부족 확인
        if (myCash.compareTo(totalCost) < 0) return "잔액이 부족합니다! (필요: " + totalCost + "원, 보유: " + myCash + "원)";

        // 5. 거래 실행
        int result = tradeDAO.buy(userId, assetId, count, currentPrice, totalCost);

        if (result > 0) {
            BigDecimal newCash = myCash.subtract(totalCost);
            member.setCash(newCash);
            return "매수를 성공했습니다.[" + symbol + "] " + count + "개를 총 " + totalCost + "원에 샀습니다.";
        } else {
            return "매수를 실패했습니다.(시스템 오류)";
        }
    }

    // 매도 로직
    public String sellCoin(String symbol, int count, MemberDTO member) {
        // 1. 코인 코드 만들기 (BTC -> KRW-BTC)
        String assetId = "KRW-" + symbol.toUpperCase();
        String userId = member.getUserId();

        // 2. 현재 가격 조회 (빗썸 API)
        BigDecimal currentPrice = coinService.getPrice(assetId);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) return "시세 조회를 실패했습니다.";

        // 3. 총 판매가격 계산
        BigDecimal countDecimal = new BigDecimal(count);
        BigDecimal totalCost = currentPrice.multiply(countDecimal);

        // 4. 내 코인 수량 확인
        int myQuantity = tradeDAO.getQuantity(userId, assetId);

        // 5. 개수 초과 확인
        if (myQuantity < count) return "개수가 초과합니다! (매도하려는 개수: " + count + "원, 보유수량: " + myQuantity + "개)";

        // 6. 거래 실행
        int result = tradeDAO.sell(userId, assetId, count, currentPrice, totalCost);
        BigDecimal myCash = member.getCash();

        if (result > 0) {
            BigDecimal newCash = myCash.add(totalCost);
            member.setCash(newCash);
            return "매도를 성공했습니다.[" + symbol + "] " + count + "개를 총 " + totalCost + "원에 팔았습니다.";
        } else {
            return "매도를 실패했습니다.(시스템 오류)";
        }
    }

    public List<TradeDTO> getTradeHistoryPaging(String userId, int page) {
        return tradeDAO.selectAll(userId, page);
    }

    public int getTotalTradeCount(String userId) {
        return tradeDAO.getTotalCount(userId);
    }
}