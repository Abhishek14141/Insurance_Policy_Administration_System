package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "premium_calculation")
@Data
public class PremiumCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculation_id")
    private Long calculationId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_id", nullable = false)
    private Quote quote;
    @Column(name = "base_premium", precision = 15, scale = 2)
    private BigDecimal basePremium;
    @Column(name = "risk_loading", precision = 15, scale = 2)
    private BigDecimal riskLoading;
    @Column(name = "rider_premium", precision = 15, scale = 2)
    private BigDecimal riderPremium;
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    @Column(name = "gst_amount", precision = 15, scale = 2)
    private BigDecimal gstAmount;
    @Column(name = "final_premium", precision = 15, scale = 2)
    private BigDecimal finalPremium;
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}
