package org.example.insurance_policy_system.repository.claimmanagement;

import org.example.insurance_policy_system.entity.claimmanagement.SurveyAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyAssignmentRepository  extends JpaRepository<SurveyAssignment ,Long> {
}
