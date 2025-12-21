package trade.service;

import coin.service.CoinService;
import member.dto.MemberDTO;
import trade.dao.TradeDAO;
import trade.dto.TradeDTO;
import util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class TradeService {
    TradeDAO tradeDAO = new TradeDAO();
    CoinService coinService = new CoinService();

    // 매수 로직
    public String buyCoin(String symbol, int count, MemberDTO member) {
        // 1. 코인 심볼 -> 자산 코드 변환 (예: BTC -> KRW-BTC)
        String assetId = "KRW-" + symbol.toUpperCase();

        // 2. 현재 시세 조회 (외부 API에서 가격 가져오기)
        BigDecimal currentPrice = coinService.getPrice(assetId);
        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) return "지원하지 않는 코인입니다: " + symbol;

        // 3. 총 매수 금액 계산 (수량 * 가격)
        BigDecimal totalCost = currentPrice.multiply(BigDecimal.valueOf(count));

        // 4. 잔액 확인: 가지고 있는 현금이 부족하면 거래 불가
        if (member.getCash().compareTo(totalCost) < 0) return "잔액이 부족합니다! (보유 투자금: "
                + member.getCash() + "원 " + "필요 금액: " + totalCost + "원)";

        try (Connection conn = DBUtil.dbconnect()) {
            conn.setAutoCommit(false); // 트랜잭션 시작 (자동 커밋 끄기)
            int result = tradeDAO.buy(conn, member.getUserId(), assetId, count, currentPrice, totalCost);
            if (result > 0) {
                member.setCash(member.getCash().subtract(totalCost)); // 매수 성공 시 현금 차감
                conn.commit(); // 트랜잭션 커밋
                return "매수를 성공했습니다.[" + symbol + "] " + count + "개를 총 " + totalCost + "원에 샀습니다.";
            } else {
                conn.rollback(); // 매수 실패 시 롤백
                return "매수를 실패했습니다.(DB 처리 중 오류 발생)";
            }
        } catch (Exception e) {
            return "매수를 실패했습니다.(시스템 오류)";
        }
    }

    // 매도 로직
    public String sellCoin(String symbol, int count, MemberDTO member) {
        // 1. 코인 심볼 -> 자산 코드 변환 (예: BTC -> KRW-BTC)
        String assetId = "KRW-" + symbol.toUpperCase();

        // 2. 현재 시세 조회
        BigDecimal currentPrice = coinService.getPrice(assetId);

        // 3. 총 매도 금액 계산 (수량 * 가격)
        BigDecimal totalCost = currentPrice.multiply(BigDecimal.valueOf(count));

        try (Connection conn = DBUtil.dbconnect()) {
            conn.setAutoCommit(false); // 트랜잭션 시작
            int myQuantity = tradeDAO.getQuantity(conn, member.getUserId(), assetId);
            if (myQuantity < count) return "개수가 초과합니다! (매도하려는 개수: " + count + "원, 보유수량: " + myQuantity + "개)";

            int result = tradeDAO.sell(conn, member.getUserId(), assetId, count, currentPrice, totalCost);
            if (result > 0) {
                member.setCash(member.getCash().add(totalCost));
                conn.commit(); // 트랜잭션 커밋
                return "매도를 성공했습니다.[" + symbol + "] " + count + "개를 총 " + totalCost + "원에 팔았습니다.";
            } else {
                conn.rollback(); // 매도 실패 시 롤백
                return "매도를 실패했습니다.(DB 처리 중 오류 발생)";
            }
        } catch (Exception e) {
            return "매도를 실패했습니다.(시스템 오류)";
        }
    }

    public List<TradeDTO> getTradeHistoryPaging(String userId, int page) {
        try (Connection conn = DBUtil.dbconnect()) {
            return tradeDAO.selectAll(conn, userId, page);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public int getTotalTradeCount(String userId) {
        try (Connection conn = DBUtil.dbconnect()) {
            return tradeDAO.getTotalCount(conn, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}