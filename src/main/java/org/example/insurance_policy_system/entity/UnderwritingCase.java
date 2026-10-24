package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.insurance_policy_system.enums.UnderwritingStatus;

import java.time.LocalDate;

@Entity
@Table(name = "underwriting_case")
@Data
public class UnderwritingCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "underwriting_id")
    private Long underwritingId;
    @Column(name = "quote_id", nullable = false, unique = true)
    private Long quoteId;
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "underwriting_date", nullable = false)
    private LocalDate underwritingDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private UnderwritingStatus status = UnderwritingStatus.PENDING;
}
