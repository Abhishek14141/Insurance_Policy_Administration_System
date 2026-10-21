package org.example.insurance_policy_system.dto.insurance_policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyHistoryResponseDto {

    private Long historyId;
    private Long policyId;
    private String action;
    private Long performedBy;
    private LocalDateTime actionDate;
    private String remarks;
}