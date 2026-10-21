package org.example.insurance_policy_system.repository.claimmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimDocumentsRepository extends JpaRepository<ClaimDocumentsRepository, Long> {
}
