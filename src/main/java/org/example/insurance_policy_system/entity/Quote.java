package org.example.insurance_policy_system.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.example.insurance_policy_system.enums.QuoteStatus;

@Entity
@Table(name = "quote", indexes = @Index(name = "idx_quote_customer", columnList = "customer_id"))
@Data
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id")
    private Long quoteId;
    @Column(name = "quote_number", unique = true, length = 40)
    private String quoteNumber;
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "policy_term", nullable = false)
    private Integer policyTerm;
    @Column(name = "sum_insured", precision = 15, scale = 2, nullable = false)
    private BigDecimal sumInsured;
    @Column(name = "premium_frequency_id")
    private Long premiumFrequencyId;
    @Column(name = "quote_date", nullable = false)
    private LocalDate quoteDate;
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "quote_status", length = 20, nullable = false)
    private QuoteStatus quoteStatus = QuoteStatus.PENDING;
}
