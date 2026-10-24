package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.MedicalUnderwriting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalUnderwritingRepository extends JpaRepository<MedicalUnderwriting,Long> {
    Optional<MedicalUnderwriting> findByUnderwritingCase_UnderwritingId(Long caseId);

}
