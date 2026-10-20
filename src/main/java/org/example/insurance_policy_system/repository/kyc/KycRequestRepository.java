package org.example.insurance_policy_system.repository.kyc;
import java.util.*;
import org.example.insurance_policy_system.entity.kyc.KycRequest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KycRequestRepository extends JpaRepository<KycRequest,Long> {
    List<KycRequest> findByCustomer_CustomerId(Long customerId);
    Optional<KycRequest> findFirstByCustomer_CustomerIdOrderByIdDesc(Long customerId);
    boolean existsByAadhaarNumber(String aadhaarNumber);
    boolean existsByPanNumberIgnoreCase(String panNumber);
}
