package org.example.insurance_policy_system.repository.undertakingRepository;

import org.example.insurance_policy_system.entity.undertaking.UnderwritingAudit;
import org.example.insurance_policy_system.entity.undertaking.UnderwritingCase;
import org.example.insurance_policy_system.enums.UnderwritingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UnderwritingCaseRepository extends JpaRepository<UnderwritingCase,Long> {

    List<UnderwritingCase> findByStatus(UnderwritingStatus status);
    boolean existsByQuoteId(Long quoteId);

}
