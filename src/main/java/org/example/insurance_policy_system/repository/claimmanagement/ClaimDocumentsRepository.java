package org.example.insurance_policy_system.repository.claimmanagement;

import org.example.insurance_policy_system.entity.claimmanagement.ClaimDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimDocumentsRepository extends JpaRepository<ClaimDocuments, Long> {



    List<ClaimDocuments> findByClaim_ClaimId(Long claimId);

}
