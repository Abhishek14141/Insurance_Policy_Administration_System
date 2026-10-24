package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class QuoteResponseDto {
    private Long quoteId;
    private String quoteNumber;
    private Long customerId;
    private Long productId;
    private Integer policyTerm;
    private BigDecimal sumInsured;
    private Long premiumFrequencyId;
    private LocalDate quoteDate;
    private LocalDate expiryDate;
    private String quoteStatus;
    private String underwritingStatus;
}
