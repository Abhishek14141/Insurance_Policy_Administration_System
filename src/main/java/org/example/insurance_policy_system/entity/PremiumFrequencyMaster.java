package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.insurance_policy_system.enums.CategoryStatus;
import org.example.insurance_policy_system.enums.PremiumFrequency;

@Entity
@Table(name = "premium_frequency")
@Data
public class PremiumFrequencyMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frequency_id")
    private Long frequencyId;
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_name", nullable = false, length = 30)
    private PremiumFrequency frequencyName;
    @Column(name = "installment_count", nullable = false)
    private Integer installmentCount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryStatus status = CategoryStatus.ACTIVE;
}
