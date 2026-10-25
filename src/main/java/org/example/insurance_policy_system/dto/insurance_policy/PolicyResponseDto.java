package org.example.insurance_policy_system.dto.insurance_policy;

import com.springproject.insurance_policy_system_policyinsurance.enums.insurance_policy.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResponseDto {

    private Long policyId;
    private String policyNo;
    private Long quoteId;
    private Long customerId;
    private Long productId;
    private LocalDate issueDate;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private PolicyStatus policyStatus;
}