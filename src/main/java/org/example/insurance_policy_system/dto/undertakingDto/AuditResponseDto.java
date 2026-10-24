package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditResponseDto {
    private Long auditId;
    private Long caseId;
    private String action;
    private String performedBy;
    private String remarks;
    private LocalDateTime actionDate;
}
