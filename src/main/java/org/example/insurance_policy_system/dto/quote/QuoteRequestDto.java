package org.example.insurance_policy_system.dto.quote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRequestDto {
    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long CustomerId;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;
    private Long policyTerm;
    private BigDecimal sumInsured;
    private Long premiumFrequency;
}
