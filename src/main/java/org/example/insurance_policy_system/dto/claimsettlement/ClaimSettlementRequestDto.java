package org.example.insurance_policy_system.dto.claimsettlement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimSettlementRequestDto {

    @NotNull(message = "payment modes is  required")
    private  String paymentMode;


}
