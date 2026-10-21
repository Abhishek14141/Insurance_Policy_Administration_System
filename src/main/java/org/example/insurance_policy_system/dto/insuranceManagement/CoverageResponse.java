package org.example.insurance_policy_system.dto.insuranceManagement;
import java.math.BigDecimal;
public record CoverageResponse(Long coverageId,Long productId,String coverageName,BigDecimal minimumCoverage,
 BigDecimal maximumCoverage,Integer waitingPeriodDays,BigDecimal deductibleAmount) {}
