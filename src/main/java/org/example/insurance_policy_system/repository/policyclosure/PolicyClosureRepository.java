
package org.example.insurance_policy_system.repository.policyclosure;

import org.example.insurance_policy_system.entity.policyclosure.PolicyClosure;
import org.example.insurance_policy_system.entity.policyclosure.PolicyClosureType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyClosureRepository extends JpaRepository<PolicyClosure, Long> {

    List<PolicyClosure> findByPolicyIdOrderByClosureDateDesc(Long policyId);

    boolean existsByPolicyIdAndClosureType(Long policyId, PolicyClosureType closureType);
}
