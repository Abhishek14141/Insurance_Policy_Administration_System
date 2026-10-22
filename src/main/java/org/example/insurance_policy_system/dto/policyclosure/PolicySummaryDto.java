package org.example.insurance_policy_system.dto.policyclosure;
import lombok.Data;
import java.time.LocalDate;
@Data public class PolicySummaryDto {
    private Long policyId;
    private String policyNo;
    private LocalDate expiryDate;
    private String policyStatus;
}
