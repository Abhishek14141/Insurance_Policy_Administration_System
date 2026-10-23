package org.example.insurance_policy_system.repository.policyservicing;



import org.example.insurance_policy_system.entity.policyservicing.PolicyRenewal;
import org.example.insurance_policy_system.enums.policyservicing.RenewalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRenewalRepository
        extends JpaRepository<PolicyRenewal, Long> {

    List<PolicyRenewal> findByPolicy_Id(Long policyId);

    List<PolicyRenewal> findByRenewalStatus(RenewalStatus renewalStatus);
}