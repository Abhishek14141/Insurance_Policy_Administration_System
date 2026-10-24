package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.insurance_policy_system.enums.QuoteRiskCategory;

@Entity
@Table(name = "risk_assessment")
@Data
public class QuoteRiskAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "risk_id")
    private Long riskId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_id", nullable = false)
    private Quote quote;
    @Column(name = "age_score")
    private Integer ageScore;
    @Column(name = "occupation_score")
    private Integer occupationScore;
    @Column(name = "medical_score")
    private Integer medicalScore;
    @Column(name = "lifestyle_score")
    private Integer lifestyleScore;
    @Column(name = "financial_score")
    private Integer  FinancialScore;
    @Column(name = "total_score")
    private Integer totalScore;
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_category", length = 20)
    private QuoteRiskCategory riskCategory;
}
