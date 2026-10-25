package org.example.insurance_policy_system.repository.undertakingRepository;

import org.example.insurance_policy_system.entity.undertaking.UnderwritingAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnderwritingAuditRepository extends JpaRepository<UnderwritingAudit, Long> {
    List<UnderwritingAudit>
    findByUnderwritingCase_UnderwritingIdOrderByActionDateDesc(Long underwritingId);}