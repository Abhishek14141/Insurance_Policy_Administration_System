package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import org.example.insurance_policy_system.enums.RiderStatus;

@Entity
@Table(name = "rider_master")
@Data
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rider_id")
    private Long riderId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private InsuranceProduct product;
    @Column(name = "rider_name", nullable = false, length = 150)
    private String riderName;
    @Column(name = "rider_description", columnDefinition = "TEXT")
    private String riderDescription;
    @Column(name = "rider_premium", precision = 15, scale = 2, nullable = false)
    private BigDecimal riderPremium;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RiderStatus status = RiderStatus.ACTIVE;
}
