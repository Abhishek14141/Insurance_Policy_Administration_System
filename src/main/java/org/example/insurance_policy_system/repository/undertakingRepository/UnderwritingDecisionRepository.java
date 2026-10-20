package org.example.insurance_policy_system.repository.undertakingRepository;

import org.example.insurance_policy_system.entity.undertaking.UnderwritingDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UnderwritingDecisionRepository extends JpaRepository<UnderwritingDecision,Long> {
    List<UnderwritingDecision> findByUnderwritingCase_UnderwritingId(Long underwritingId);}
