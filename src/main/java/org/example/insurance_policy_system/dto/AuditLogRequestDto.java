package org.example.insurance_policy_system.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogRequestDto {
    private String module;
    private String entityType;
    private Long entityId;
    private String action;
    private Long performedBy;
    private String remarks;
}
