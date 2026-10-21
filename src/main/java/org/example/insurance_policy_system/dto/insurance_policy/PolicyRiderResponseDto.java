package org.example.insurance_policy_system.dto.insurance_policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRiderResponseDto {

    private Long policyRiderId;
    private Long policyId;
    private Long riderId;
    private BigDecimal riderPremium;
}