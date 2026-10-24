package org.example.insurance_policy_system.dto.policyservicing;




import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ServiceRequestHistoryResponseDTO {

    private Long historyId;
    private Long requestId;
    private String action;
    private Long actionById;
    private LocalDateTime actionDate;
    private String remarks;
}
