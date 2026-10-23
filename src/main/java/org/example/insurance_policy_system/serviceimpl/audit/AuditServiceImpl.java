
package org.example.insurance_policy_system.serviceimpl.audit;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.dto.audit.AuditLogResponseDto;
import org.example.insurance_policy_system.entity.audit.SystemAuditLog;
import org.example.insurance_policy_system.exception.BadRequestException;
import org.example.insurance_policy_system.exception.ResourceNotFoundException;
import org.example.insurance_policy_system.repository.audit.SystemAuditLogRepository;
import org.example.insurance_policy_system.service.audit.AuditService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final SystemAuditLogRepository auditRepository;
    private final ModelMapper modelMapper;

    @Override
    public void logAction(Long userId, String moduleName, String action, String oldValue, String newValue, String ipAddress, String remarks) {

        if (moduleName == null || moduleName.isBlank()) {
            throw new BadRequestException("Module name is required");
        }

        if (action == null || action.isBlank()) {
            throw new BadRequestException("Audit action is required");
        }

        SystemAuditLog auditLog = SystemAuditLog.builder()
                .userId(userId)
                .moduleName(moduleName.trim())
                .action(action.trim())
                .oldValue(oldValue)
                .newValue(newValue)
                .ipAddress(ipAddress)
                .remarks(remarks)
                .build();

        auditRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getAllLogs() {
        return auditRepository.findAllByOrderByActionTimeDesc()
                .stream()
                .map(log -> modelMapper.map(
                        log, AuditLogResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponseDto getLogById(Long auditId) {
        SystemAuditLog log = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Audit log not found with ID: " + auditId));

        return modelMapper.map(log, AuditLogResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getLogsByModule(String moduleName) {
        if (moduleName == null || moduleName.isBlank()) {
            throw new BadRequestException("Module name is required");
        }

        return auditRepository
                .findByModuleNameIgnoreCaseOrderByActionTimeDesc(moduleName)
                .stream()
                .map(log -> modelMapper.map(
                        log, AuditLogResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getLogsByUser(Long userId) {
        return auditRepository.findByUserIdOrderByActionTimeDesc(userId)
                .stream()
                .map(log -> modelMapper.map(
                        log, AuditLogResponseDto.class))
                .toList();
    }
}
