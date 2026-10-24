package org.example.insurance_policy_system.dto.insuranceManagement;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BenefitRequest(@NotBlank @Size(max=150) String benefitName, String benefitDescription,
 @DecimalMin("0.00") BigDecimal benefitLimit) {}
