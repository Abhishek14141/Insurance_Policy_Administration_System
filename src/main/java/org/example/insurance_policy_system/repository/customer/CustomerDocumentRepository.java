package org.example.insurance_policy_system.repository.customer;


import org.example.insurance_policy_system.entity.customer.CustomerDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Long> {

    List<CustomerDocument> findByCustomer_CustomerId(Long customerId);
}
