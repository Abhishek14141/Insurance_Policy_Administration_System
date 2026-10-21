package org.example.insurance_policy_system.dto.insurance_policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCoverageResponseDto {

    private Long coverageId;
    private Long policyId;
    private String coverageName;
    private BigDecimal sumInsured;
    private LocalDate coverageStartDate;
    private LocalDate coverageEndDate;
}