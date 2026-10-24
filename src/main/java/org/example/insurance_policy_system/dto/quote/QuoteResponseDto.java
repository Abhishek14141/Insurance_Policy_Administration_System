package org.example.insurance_policy_system.dto.quote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponseDto {
    private Long quoteId;
    private String quoteNumber;
    private Long customerId;
    private Long productId;
    private Long policyTerm;
    private BigDecimal sumInsured;
    private Long premiumFrequency;
    private LocalDate quoteDate;
    private LocalDate quoteExpiryDate;
    private QuoteStatus quoteStatus;
}
