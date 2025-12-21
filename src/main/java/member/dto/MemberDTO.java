package member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDTO {

    @NonNull private String userId;
    @NonNull private String password;
    private BigDecimal cash;

    @Override
    public String toString() {
        if (cash == null) return "보유 투자금 : 0";
        return "보유 투자금 : " + String.format("%,d", cash.toBigInteger());
    }
}
