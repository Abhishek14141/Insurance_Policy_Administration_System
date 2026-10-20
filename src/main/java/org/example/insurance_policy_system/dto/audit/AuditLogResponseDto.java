
package org.example.insurance_policy_system.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDto {

    private Long auditId;
    private Long userId;
    private String moduleName;
    private String action;
    private String oldValue;
    private String newValue;
    private String ipAddress;
    private String remarks;
    private LocalDateTime actionTime;
}
