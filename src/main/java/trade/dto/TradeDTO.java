package trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeDTO {

    private int tradeId;
    private String tradeType;
    private int tradeQuantity;
    private BigDecimal tradePrice;
    private Date tradeDate;
    private String userId;
    private String assetId;
}
