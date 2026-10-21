package org.example.insurance_policy_system.dto.policyservicing;


import lombok.*;
import org.example.insurance_policy_system.enums.policyservicing.RenewalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyRenewalResponseDTO {

    private Long renewalId;
    private Long policyId;
    private LocalDate renewalDate;
    private LocalDate newExpiryDate;
    private BigDecimal premiumAmount;
    private RenewalStatus renewalStatus;
}

