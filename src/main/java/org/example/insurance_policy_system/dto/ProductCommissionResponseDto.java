package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCommissionResponseDto {
    private Long commissionId;
    private Long productId;
    private BigDecimal firstYearCommission;
    private BigDecimal renewalCommission;
    private BigDecimal branchCommission;
}
