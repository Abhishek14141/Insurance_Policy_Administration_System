package org.example.insurance_policy_system.repository.policyservicing;


import org.example.insurance_policy_system.entity.policyservicing.ServiceRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestHistoryRepository
        extends JpaRepository<ServiceRequestHistory, Long> {

    List<ServiceRequestHistory> findByServiceRequest_RequestIdOrderByActionDateDesc(
            Long requestId);

    List<ServiceRequestHistory> findByActionBy_Id(Long employeeId);
}
