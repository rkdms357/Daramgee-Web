package asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssetDTO {

    private String assetId;
    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    private double changeRate;
}
