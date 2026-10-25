package org.example.insurance_policy_system.entity.insuranceManagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.insuranceManagement.PremiumFrequencyStatus;


@Entity
@Table(name = "premium_frequency")
@Getter
@Setter
@NoArgsConstructor
public class PremiumFrequency {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "frequency_id")
 private Long frequencyId;

 @Column(name = "frequency_name", nullable = false, unique = true, length = 30)
 private String frequencyName;

 @Column(name = "installment_count", nullable = false)
 private Integer installmentCount;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 20)
 private PremiumFrequencyStatus status = PremiumFrequencyStatus.ACTIVE;
}