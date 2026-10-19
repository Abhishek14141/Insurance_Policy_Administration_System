package org.example.insurance_policy_system.entity.undertaking;

import jakarta.persistence.*;
import org.example.insurance_policy_system.enums.RiskLevel;

@Entity
@Table(name = "risk_score")
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "risk_id")
    private Long riskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Column(name = "medical_score")
    private Integer medicalScore;

    @Column(name = "occupation_score")
    private Integer occupationScore;

    @Column(name = "lifestyle_score")
    private Integer lifestyleScore;

    @Column(name = "financial_score")
    private Integer financialScore;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "risk_category")
    private RiskLevel riskCategory;

}