package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAssessmentRequestDto {

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal assessedAmount;

    private Long coverageId;
//
//    @NotNull
//    @DecimalMin("0.00")
//    private BigDecimal deductible;

//    @NotNull
//    @DecimalMin("0.00")
//    private BigDecimal depreciation;
//
//    @NotNull
//    @DecimalMin("0.00")
//    private BigDecimal nonPayable;
}