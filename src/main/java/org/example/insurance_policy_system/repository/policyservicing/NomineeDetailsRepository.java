package org.example.insurance_policy_system.repository.policyservicing;


import org.example.insurance_policy_system.entity.policyservicing.NomineeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NomineeDetailsRepository
        extends JpaRepository<NomineeDetails, Long> {

    List<NomineeDetails> findByPolicy_Id(Long policyId);
}
