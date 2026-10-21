package org.example.insurance_policy_system.repository.policyservicing;



import org.example.insurance_policy_system.entity.policyservicing.PolicyEndorsement;
import org.example.insurance_policy_system.enums.policyservicing.EndorsementApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyEndorsementRepository
        extends JpaRepository<PolicyEndorsement, Long> {

    Optional<PolicyEndorsement> findByEndorsementNumber(
            String endorsementNumber);

    List<PolicyEndorsement> findByServiceRequest_RequestId(Long requestId);

    List<PolicyEndorsement> findByApprovalStatus(
            EndorsementApprovalStatus approvalStatus);

    boolean existsByEndorsementNumber(String endorsementNumber);
}