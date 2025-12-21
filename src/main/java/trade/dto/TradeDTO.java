package trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeDTO {

    @NonNull private String userId;
    @NonNull private String assetId;
    @NonNull private String tradeType;
    @NonNull private int tradeQuantity;
    @NonNull private BigDecimal tradePrice;
    @NonNull private Timestamp tradeDate;
}
