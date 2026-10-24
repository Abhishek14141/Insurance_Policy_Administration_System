package org.example.insurance_policy_system.dto;
import lombok.Data; import java.math.BigDecimal;
@Data public class CoverageResponseDto {
 private Long coverageId;
 private Long productId;
 private String coverageName;
 private BigDecimal minimumCoverage;
 private BigDecimal maximumCoverage;
 private Integer waitingPeriodDays;
 private BigDecimal deductibleAmount;
}
