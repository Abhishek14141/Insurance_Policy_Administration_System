package org.example.insurance_policy_system.dto.insurance_policy;

import com.springproject.insurance_policy_system_policyinsurance.enums.insurance_policy.PolicyStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRequestDto {

    @NotNull(message = "Quote ID is required")
    @Positive(message = "Quote ID must be positive")
    private Long quoteId;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private PolicyStatus policyStatus;
}