package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.FinancialUnderwriting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialUnderwritingRepository extends JpaRepository<FinancialUnderwriting,Long> {
    Optional<FinancialUnderwriting> findByUnderwritingCase_UnderwritingId(Long underwritingId);
}
