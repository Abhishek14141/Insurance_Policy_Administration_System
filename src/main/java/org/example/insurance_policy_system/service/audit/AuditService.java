
package org.example.insurance_policy_system.service.audit;

import org.example.insurance_policy_system.dto.audit.AuditLogResponseDto;

import java.util.List;

public interface AuditService {

    void logAction(Long userId, String moduleName, String action, String oldValue, String newValue, String ipAddress, String remarks);

    List<AuditLogResponseDto> getAllLogs();
    AuditLogResponseDto getLogById(Long auditId);
    List<AuditLogResponseDto> getLogsByModule(String moduleName);
    List<AuditLogResponseDto> getLogsByUser(Long userId);
}
