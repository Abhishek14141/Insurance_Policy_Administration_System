package org.example.insurance_policy_system.repository.auth;

import java.util.List;

import org.example.insurance_policy_system.entity.auth.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<AuditLog> findByModuleNameOrderByCreatedAtDesc(
            String moduleName);
}