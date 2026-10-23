package org.example.insurance_policy_system.entity.policyservicing;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.insurance_policy_system.entity.policy_insurance.Policy;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_cancellation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancellation_id")
    private Long cancellationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "cancellation_date", nullable = false)
    private LocalDate cancellationDate;

    @Column(name = "cancellation_reason", nullable = false, columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "refund_amount", precision = 12, scale = 2)
    private BigDecimal refundAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;
}

