package org.example.insurance_policy_system.entity.customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Nominee")
@Data
@NoArgsConstructor
public class Nominee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nominee_id")
    private Long nomineeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "nominee_name", nullable = false)
    private String nomineeName;

    @Column(nullable = false)
    private String relationship;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "share_percentage",
            precision = 5, scale = 2, nullable = false)
    private BigDecimal sharePercentage;
}
