
package org.example.insurance_policy_system.repository.kyc;

import java.util.List;

import org.example.insurance_policy_system.entity.kyc.KycAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KycAuditLogRepository
        extends JpaRepository<KycAuditLog, Long> {

    List<KycAuditLog> findByKyc_IdOrderByActionDateDesc(Long kycId);

}
