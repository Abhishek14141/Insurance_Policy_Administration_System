package org.example.insurance_policy_system.dto.insuranceManagement;
import java.math.BigDecimal;
public record BenefitResponse(Long benefitId,Long productId,String benefitName,String benefitDescription,BigDecimal benefitLimit) {}
