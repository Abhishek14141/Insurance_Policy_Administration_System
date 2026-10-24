package org.example.insurance_policy_system.repository.customer;

import org.example.insurance_policy_system.entity.customer.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
    List<AuditLog> findByCustomerIdOrderByTimestampDesc(Long customerId); }
