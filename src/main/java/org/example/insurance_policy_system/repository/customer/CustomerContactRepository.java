package org.example.insurance_policy_system.repository.customer;

import java.util.Optional;

import org.example.insurance_policy_system.entity.customer.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long> {

    Optional<CustomerContact> findByCustomer_CustomerId(Long customerId);
}
