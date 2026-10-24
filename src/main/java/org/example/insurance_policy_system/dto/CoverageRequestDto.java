package org.example.insurance_policy_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
public class CoverageRequestDto {
    @NotBlank
    private String coverageName;
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal minimumCoverage;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal maximumCoverage;
    @NotNull
    @Min(0)
    private Integer waitingPeriodDays;
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal deductibleAmount;
}
