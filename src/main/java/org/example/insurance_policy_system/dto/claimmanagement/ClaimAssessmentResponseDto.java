package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAssessmentResponseDto {

    private Long assessmentId;
    private Long claimId;
    private BigDecimal assessedAmount;
    private BigDecimal deductible;
    private BigDecimal depreciation;
    private BigDecimal nonPayable;
    private BigDecimal netPayable;
}