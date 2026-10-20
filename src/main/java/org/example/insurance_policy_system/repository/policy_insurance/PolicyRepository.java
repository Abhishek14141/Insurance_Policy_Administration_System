package org.example.insurance_policy_system.repository.policy_insurance;


import com.springproject.insurance_policy_system.entity.policy_insurance.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
