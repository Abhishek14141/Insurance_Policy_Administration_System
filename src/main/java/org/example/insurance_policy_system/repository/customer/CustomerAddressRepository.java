package org.example.insurance_policy_system.repository.customer;

import java.util.List;

import org.example.insurance_policy_system.entity.customer.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByCustomer_CustomerId(Long customerId);
}
