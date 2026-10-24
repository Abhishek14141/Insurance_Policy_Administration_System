package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.insurance_policy_system.enums.UnderwritingDecisionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "underwriting_decision")
@Data
public class UnderwritingDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decision_id")
    private Long decisionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private UnderwritingDecisionStatus decision;

    @Column(name = "loading_percentage", precision = 5, scale = 2)
    private BigDecimal loadingPercentage;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

}
