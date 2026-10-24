package org.example.insurance_policy_system.entity.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.customer.EmploymentType;
import org.example.insurance_policy_system.enums.customer.Occupation;


import java.math.BigDecimal;

@Entity
@Table(name = "customer_occupation")
@Data
@NoArgsConstructor
public class CustomerOccupation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "occupation_id")
    private Long occupationId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false, length = 50)
    private Occupation occupation;

    @Column(name = "employer_name", length = 255)
    private String employerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 30)
    private EmploymentType employmentType;

    @Column(name = "annual_income", precision = 15, scale = 2, nullable = false)
    private BigDecimal annualIncome;
}
