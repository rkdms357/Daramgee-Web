package coin.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceDTO {
    private BigDecimal price;   // 현재가
    private double changeRate;  // 변동률
}