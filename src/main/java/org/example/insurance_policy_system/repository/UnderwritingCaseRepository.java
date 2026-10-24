package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.UnderwritingCase;
import org.example.insurance_policy_system.enums.UnderwritingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnderwritingCaseRepository extends JpaRepository<UnderwritingCase,Long> {

    List<UnderwritingCase> findByStatus(UnderwritingStatus status);
    boolean existsByQuoteId(Long quoteId);

    Optional<UnderwritingCase> findByQuoteId(Long quoteId);}
