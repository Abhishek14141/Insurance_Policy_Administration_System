package org.example.insurance_policy_system.entity.quote;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountMasterId;

    private String discountName;
    private Double discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private DiscountMasterStatus status;
}
