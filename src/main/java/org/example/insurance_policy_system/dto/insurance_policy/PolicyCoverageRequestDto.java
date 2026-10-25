package org.example.insurance_policy_system.dto.insurance_policy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCoverageRequestDto {

    @NotNull(message = "Policy ID is required")
    @Positive(message = "Policy ID must be positive")
    private Long policyId;

    @NotBlank(message = "Coverage name is required")
    private String coverageName;

    @NotNull(message = "Sum insured is required")
    @Positive(message = "Sum insured must be greater than zero")
    private BigDecimal sumInsured;

    @NotNull(message = "Coverage start date is required")
    private LocalDate coverageStartDate;

    @NotNull(message = "Coverage end date is required")
    private LocalDate coverageEndDate;
}