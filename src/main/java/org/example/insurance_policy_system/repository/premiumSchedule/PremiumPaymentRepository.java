package org.example.insurance_policy_system.repository.premiumSchedule;

import com.ampta.premiumservice.entity.PremiumPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumPaymentRepository extends JpaRepository<PremiumPayment, Long> {
    List<PremiumPayment> findByPolicyIdOrderByPaymentDateDesc(Long policyId);
    boolean existsByTransactionReference(String txnRef);
    Optional<PremiumPayment> findByTransactionReference(String txnRef);
}
