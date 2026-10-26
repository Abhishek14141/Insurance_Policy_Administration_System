package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.example.insurance_policy_system.enums.DiscountStatus;

@Entity
@Table(name = "discount_master")
@Data
public class DiscountMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;
    @Column(name = "discount_name", nullable = false, length = 100)
    private String discountName;
    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    @Column(name = "valid_from")
    private LocalDate validFrom;
    @Column(name = "valid_to")
    private LocalDate validTo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountStatus status = DiscountStatus.ACTIVE;
}
