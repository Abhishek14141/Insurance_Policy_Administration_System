package org.example.insurance_policy_system.repository.undertakingRepository;

import org.example.insurance_policy_system.entity.undertaking.UnderwritingAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnderwritingAuditLogRepository extends JpaRepository<UnderwritingAuditLog,Long> {
    List<UnderwritingAuditLog> findByUnderwritingCase_UnderwritingId(Long underwritingId);

}

