package org.example.insurance_policy_system.client;

import lombok.Data;

@Data
public class PolicyEligibilityDto {

    private Long policyId;
    private Long customerId;

    private Boolean active;
    private Boolean withinCoveragePeriod;
    private Boolean waitingPeriodCompleted;
    private Boolean claimEligible;

    private String rejectionReason;
}