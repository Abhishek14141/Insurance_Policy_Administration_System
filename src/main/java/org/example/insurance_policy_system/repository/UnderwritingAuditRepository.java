package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.UnderwritingAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnderwritingAuditRepository extends JpaRepository<UnderwritingAudit, Long> {
    List<UnderwritingAudit>
    findByUnderwritingCase_UnderwritingIdOrderByActionDateDesc(Long underwritingId);}
