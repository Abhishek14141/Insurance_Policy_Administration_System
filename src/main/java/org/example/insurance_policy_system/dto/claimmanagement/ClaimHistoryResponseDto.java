package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimHistoryResponseDto {

    private Long historyId;
    private Long claimId;
    private String action;
    private Long performedBy;
    private LocalDateTime actionDate;
    private String remarks;
}