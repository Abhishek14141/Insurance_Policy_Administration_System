package org.example.insurance_policy_system.entity.policy_insurance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.policy_insurance.EndorsementType;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEndorsement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endorsementId;
    private Long policyId;
    private EndorsementType endorsementType;
    private String oldValue;
    private String newValue;
    private Long approvedBy;// Employee
    private LocalDateTime endorsementDate;

}
