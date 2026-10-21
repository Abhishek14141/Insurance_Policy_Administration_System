package org.example.insurance_policy_system.repository.undertakingRepository;

import org.example.insurance_policy_system.entity.undertaking.FinancialUnderwriting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialUnderwritingRepository extends JpaRepository<FinancialUnderwriting,Long> {
    List<FinancialUnderwriting> findByUnderwritingCase_UnderwritingId(Long underwritingId);
}
