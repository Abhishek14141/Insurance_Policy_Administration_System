package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.UnderwritingDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnderwritingDecisionRepository extends JpaRepository<UnderwritingDecision,Long> {
    Optional<UnderwritingDecision> findByUnderwritingCase_UnderwritingId(Long underwritingId);}
