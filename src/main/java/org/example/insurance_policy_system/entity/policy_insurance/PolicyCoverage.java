package org.example.insurance_policy_system.entity.policy_insurance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCoverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coverageId;
    private Long policyId;
    private String coverageName;
    private BigDecimal sumInsured;
    private LocalDate coverageStartDate;
    private LocalDate coverageEndDate;




}
