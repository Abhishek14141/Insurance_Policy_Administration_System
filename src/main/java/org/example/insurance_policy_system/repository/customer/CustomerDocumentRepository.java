package org.example.insurance_policy_system.repository.customer;

import java.util.List;

import org.example.insurance_policy_system.entity.customer.CustomerDocument;
import org.example.insurance_policy_system.enums.customer.DocumentVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Long> {

    List<CustomerDocument> findByCustomer_CustomerId(Long customerId);

    List<CustomerDocument> findByCustomer_CustomerIdAndVerificationStatus(
            Long customerId,
            DocumentVerificationStatus verificationStatus);
}