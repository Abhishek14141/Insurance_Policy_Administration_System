package org.example.insurance_policy_system.client;

import lombok.Data;

@Data
public class PremiumEligibilityDto {

    private Long policyId;
    private Boolean premiumsUpToDate;
    private Boolean withinGracePeriod;
    private Boolean eligibleForClaim;
    private String reason;
}