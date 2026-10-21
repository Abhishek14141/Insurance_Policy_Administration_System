package org.example.insurance_policy_system.dto.policyservicing;



import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyCancellationResponseDTO {

    private Long cancellationId;
    private Long policyId;
    private LocalDate cancellationDate;
    private String cancellationReason;
    private BigDecimal refundAmount;
    private Long approvedById;
}

