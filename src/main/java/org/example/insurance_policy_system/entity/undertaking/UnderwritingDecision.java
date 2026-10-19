package org.example.insurance_policy_system.entity.undertaking;

import jakarta.persistence.*;
import org.example.insurance_policy_system.enums.UnderwritingDecisionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "underwriting_decision")
public class UnderwritingDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decision_id")
    private Long decisionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Column(name = "decision")
    private UnderwritingDecisionStatus decision;

    @Column(name = "loading_percentage", precision = 5, scale = 2)
    private BigDecimal loadingPercentage;

    @Column(name = "exclusion_details", columnDefinition = "TEXT")
    private String exclusionDetails;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

}