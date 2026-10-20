package org.example.insurance_policy_system.dto.insuranceManagement;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CoverageRequest(@NotBlank @Size(max=150) String coverageName,
 @NotNull @DecimalMin("0.00") BigDecimal minimumCoverage,
 @NotNull @DecimalMin("0.00") BigDecimal maximumCoverage,
 @Min(0) Integer waitingPeriodDays,
 @DecimalMin("0.00") BigDecimal deductibleAmount) {}
