package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "claim_assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private Long assessmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
    private ClaimMaster claim;

    @Column(name = "assessed_amount", precision = 15, scale = 2)
    private BigDecimal assessedAmount;

    @Column(name = "deductible", precision = 15, scale = 2)
    private BigDecimal deductible;

    @Column(name = "depreciation", precision = 15, scale = 2)
    private BigDecimal depreciation;

    @Column(name = "non_payable", precision = 15, scale = 2)
    private BigDecimal nonPayable;

    @Column(name = "net_payable", precision = 15, scale = 2)
    private BigDecimal netPayable;

}
