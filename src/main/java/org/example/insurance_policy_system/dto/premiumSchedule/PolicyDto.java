package org.example.insurance_policy_system.dto.premiumSchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDto {
    private Long policyId;
    private String policyNumber;
    private Long customerId;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String premiumFrequency;
    private BigDecimal finalPremium;
}
