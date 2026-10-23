package org.example.insurance_policy_system.dto.insuranceManagement;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CommissionRequest(@NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal firstYearCommission,
 @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal renewalCommission,
 @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal branchCommission) {}
