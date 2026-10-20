package org.example.insurance_policy_system.repository.customer;
import java.util.List;

import org.example.insurance_policy_system.entity.customer.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {

    List<Nominee> findByCustomer_CustomerId(Long customerId);
}
