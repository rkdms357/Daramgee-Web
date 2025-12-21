package asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssetDTO {

    @NonNull private String assetId;
    @NonNull private String name;
    @NonNull private BigDecimal currentPrice;
    @NonNull private double changeRate;

    public String getSymbol() {
        if (assetId == null || !assetId.contains("-")) return "";
        return assetId.split("-")[1];
    }
}
