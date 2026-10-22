package org.example.insurance_policy_system.entity.insuranceManagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.insuranceManagement.RiderStatus;

import java.math.BigDecimal;


@Entity
@Table(name = "rider_master")
@Getter
@Setter
@NoArgsConstructor
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

 @Column(name = "rider_premium", nullable = false, precision = 15, scale = 2)
 private BigDecimal riderPremium = BigDecimal.ZERO;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 20)
 private RiderStatus status = RiderStatus.ACTIVE;
}