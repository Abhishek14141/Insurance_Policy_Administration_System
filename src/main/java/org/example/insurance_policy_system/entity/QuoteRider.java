package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "quote_rider")
@Data
public class QuoteRider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rider_selection_id")
    private Long riderSelectionId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_id", nullable = false)
    private Quote quote;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;
    @Column(name = "rider_premium", precision = 15, scale = 2)
    private BigDecimal riderPremium;
}
