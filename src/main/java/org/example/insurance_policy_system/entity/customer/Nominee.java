package org.example.insurance_policy_system.entity.customer;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nominee")
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

    @Column(name = "nominee_name", nullable = false, length = 150)
    private String nomineeName;

    @Column(nullable = false, length = 50)
    private String relationship;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "share_percentage",
            nullable = false, precision = 5, scale = 2)
    private BigDecimal sharePercentage;
}
