package portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortfolioDTO {
    @NonNull private int portfolioId;
    private int quantity;
    private BigDecimal avgPrice;
    @NonNull private String userId;
    private String assetId;

    // 화면 출력을 위해 추가
    private String name;              // 코인명
    private BigDecimal currentPrice;  // 현재 시세 (빗썸 API)
    private BigDecimal totalValue;    // 평가 금액 (현재가 * 수량)
    private BigDecimal profit;        // 평가 손익 (평가금액 - 매수금액)
    private double yield;             // 수익률
}
