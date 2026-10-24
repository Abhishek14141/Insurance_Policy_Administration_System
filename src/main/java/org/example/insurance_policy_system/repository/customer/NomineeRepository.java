package org.example.insurance_policy_system.repository.customer;

import org.example.insurance_policy_system.entity.customer.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {

    List<Nominee> findByCustomer_CustomerId(Long customerId);
}
