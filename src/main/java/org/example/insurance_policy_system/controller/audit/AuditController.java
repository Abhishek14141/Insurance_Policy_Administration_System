
package org.example.insurance_policy_system.controller.audit;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.audit.AuditLogResponseDto;
import org.example.insurance_policy_system.service.audit.AuditService;
import org.example.insurance_policy_system.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Validated
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogResponseDto>>> getAllLogs() {
        return ResponseEntity.ok(ApiResponse.success("Audit logs fetched successfully", auditService.getAllLogs()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuditLogResponseDto>> getLogById(@PathVariable @Positive Long id) {

        return ResponseEntity.ok(ApiResponse.success("Audit log fetched successfully", auditService.getLogById(id)));
    }

    @GetMapping("/module/{moduleName}")
    public ResponseEntity<ApiResponse<List<AuditLogResponseDto>>> getLogsByModule(@PathVariable String moduleName) {

        return ResponseEntity.ok(
                ApiResponse.success("Module audit logs fetched successfully", auditService.getLogsByModule(moduleName)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AuditLogResponseDto>>> getLogsByUser(@PathVariable @Positive Long userId) {

        return ResponseEntity.ok(
                ApiResponse.success("User audit logs fetched successfully",
                        auditService.getLogsByUser(userId)));
    }
}
