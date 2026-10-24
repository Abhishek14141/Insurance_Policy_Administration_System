package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class QuoteRequestDto {
    @NotNull
    @Positive
    private Long customerId;
    @NotNull
    @Positive
    private Long productId;
    @NotNull
    @Positive
    private Integer policyTerm;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal sumInsured;
    @NotNull
    @Positive
    private Long premiumFrequencyId;
}
