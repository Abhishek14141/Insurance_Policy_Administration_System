package org.example.insurance_policy_system.entity.quote;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calculationId;

    private Long quoteId ;

    private BigDecimal basePremium;
    private BigDecimal riskLoading;
    private BigDecimal riderPremium;
    private BigDecimal discountAmount;
    private BigDecimal gstAmount;
    private BigDecimal finalPremium;




}
