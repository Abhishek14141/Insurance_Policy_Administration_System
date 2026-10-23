package org.example.insurance_policy_system.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PolicyCoverageDto {

    private Long policyId;
    private BigDecimal sumInsured;
    private BigDecimal remainingCoverage;
    private BigDecimal deductible;
}