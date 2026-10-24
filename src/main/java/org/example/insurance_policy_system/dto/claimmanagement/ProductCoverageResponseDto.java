package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCoverageResponseDto {

    private Long coverageId;

    private Long productId;

    private String coverageName;

    private BigDecimal minimumCoverage;

    private BigDecimal maximumCoverage;

    private Integer waitingPeriodDays;

    private BigDecimal deductibleAmount;
}