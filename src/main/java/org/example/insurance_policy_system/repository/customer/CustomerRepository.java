package org.example.insurance_policy_system.repository.customer;

import java.util.Optional;

import org.example.insurance_policy_system.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<Customer> findByEmailIgnoreCase(String email);

    Optional<Customer> findByMobileNumber(String mobileNumber);
}
