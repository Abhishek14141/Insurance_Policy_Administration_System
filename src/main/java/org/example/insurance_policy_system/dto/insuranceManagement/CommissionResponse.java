package org.example.insurance_policy_system.dto.insuranceManagement;
import java.math.BigDecimal;
public record CommissionResponse(Long commissionId,Long productId,BigDecimal firstYearCommission,BigDecimal renewalCommission,BigDecimal branchCommission) {}
