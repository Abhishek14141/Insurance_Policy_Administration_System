package org.example.insurance_policy_system.repository.policyservicing;



import org.example.insurance_policy_system.entity.policyservicing.PolicyCancellation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyCancellationRepository
        extends JpaRepository<PolicyCancellation, Long> {

    List<PolicyCancellation> findByPolicy_Id(Long policyId);

    List<PolicyCancellation> findByApprovedBy_Id(Long employeeId);
}
