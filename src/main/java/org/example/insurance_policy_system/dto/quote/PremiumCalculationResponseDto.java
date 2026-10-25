package org.example.insurance_policy_system.dto.quote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumCalculationResponseDto {

    private Long calculationId;
    private Long quoteId;

    private BigDecimal basePremium;
    private BigDecimal riskLoading;
    private BigDecimal riderPremium;
    private BigDecimal discountAmount;
    private BigDecimal gstAmount;
    private BigDecimal finalPremium;
}