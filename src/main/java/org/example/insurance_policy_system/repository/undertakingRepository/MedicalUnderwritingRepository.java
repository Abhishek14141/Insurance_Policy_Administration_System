package org.example.insurance_policy_system.repository.undertakingRepository;

import org.example.insurance_policy_system.entity.undertaking.MedicalUnderwriting;
import org.example.insurance_policy_system.entity.undertaking.UnderwritingCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalUnderwritingRepository extends JpaRepository<MedicalUnderwriting,Long> {
    List<MedicalUnderwriting> findByUnderwritingCase_UnderwritingId(Long underwritingId);
    Optional<MedicalUnderwriting> findFirstByUnderwritingCase(UnderwritingCase underwritingCase);

}
