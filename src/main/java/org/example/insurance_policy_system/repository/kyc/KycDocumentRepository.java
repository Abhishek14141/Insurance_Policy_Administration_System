package org.example.insurance_policy_system.repository.kyc;
import java.util.*;
import org.example.insurance_policy_system.entity.kyc.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KycDocumentRepository extends JpaRepository<KycDocument,Long> {
    List<KycDocument> findByKyc_Id(Long kycId);
    boolean existsByDocumentNumber(String documentNumber);
}
