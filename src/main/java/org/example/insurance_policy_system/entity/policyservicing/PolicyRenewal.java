package org.example.insurance_policy_system.entity.policyservicing;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.insurance_policy_system.entity.policy_insurance.Policy;
import org.example.insurance_policy_system.enums.policyservicing.RenewalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_renewal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRenewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "renewal_id")
    private Long renewalId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    @Column(name = "new_expiry_date", nullable = false)
    private LocalDate newExpiryDate;

    @Column(name = "premium_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal premiumAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "renewal_status", nullable = false)
    private RenewalStatus renewalStatus = RenewalStatus.PENDING;
}
