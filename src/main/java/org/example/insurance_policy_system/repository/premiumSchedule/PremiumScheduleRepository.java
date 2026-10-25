package org.example.insurance_policy_system.repository.premiumSchedule;

import com.ampta.premiumservice.entity.PremiumSchedule;
import com.ampta.premiumservice.entity.enums.InstallmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumScheduleRepository extends JpaRepository<PremiumSchedule, Long> {
    List<PremiumSchedule> findByPolicyIdOrderByInstallmentNoAsc(Long policyId);
    List<PremiumSchedule> findByStatusAndDueDateBefore(InstallmentStatus status, LocalDate date);
    Optional<PremiumSchedule> findByPolicyIdAndInstallmentNo(Long policyId, Integer installmentNo);

}
