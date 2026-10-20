package org.example.insurance_policy_system.dto.policyservicing;


import lombok.*;
import org.example.insurance_policy_system.enums.policyservicing.ServiceRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestResponseDTO {

    private Long requestId;
    private String requestNumber;
    private Long policyId;
    private Long customerId;
    private String requestType;
    private LocalDateTime requestDate;
    private ServiceRequestStatus status;
}

