package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PremiumCalculationResponseDto {
    private Long calculationId;
    private Long quoteId;
    private BigDecimal basePremium;
    private BigDecimal riskLoading;
    private BigDecimal riderPremium;
    private BigDecimal discountAmount;
    private BigDecimal gstAmount;
    private BigDecimal finalPremium;
    private LocalDateTime calculatedAt;
}
