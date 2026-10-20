
package org.example.insurance_policy_system.repository.audit;

import org.example.insurance_policy_system.entity.audit.SystemAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemAuditLogRepository extends JpaRepository<SystemAuditLog, Long> {

    List<SystemAuditLog> findAllByOrderByActionTimeDesc();
    List<SystemAuditLog> findByModuleNameIgnoreCaseOrderByActionTimeDesc(String moduleName);
    List<SystemAuditLog> findByUserIdOrderByActionTimeDesc(Long userId);
}
