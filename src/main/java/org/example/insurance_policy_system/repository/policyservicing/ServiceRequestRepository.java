package org.example.insurance_policy_system.repository.policyservicing;


import org.example.insurance_policy_system.entity.policyservicing.ServiceRequest;
import org.example.insurance_policy_system.enums.policyservicing.ServiceRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

    Optional<ServiceRequest> findByRequestNumber(String requestNumber);

    List<ServiceRequest> findByCustomer_Id(Long customerId);

    List<ServiceRequest> findByPolicy_Id(Long policyId);

    List<ServiceRequest> findByStatus(ServiceRequestStatus status);

    boolean existsByRequestNumber(String requestNumber);
}
