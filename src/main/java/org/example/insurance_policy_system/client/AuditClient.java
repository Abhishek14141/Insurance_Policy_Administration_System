package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.AuditLogRequestDto;
import org.example.insurance_policy_system.dto.AuditResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "audit-service-client")
public interface AuditClient {
    @GetMapping("/api/audit/underwriting/{caseId}")
    List<AuditResponseDto> getUnderwritingAuditHistory(@PathVariable("caseId") Long caseId);

    @PostMapping("/api/audit/logs")
    void writeAuditLog(@RequestBody AuditLogRequestDto request);
}
