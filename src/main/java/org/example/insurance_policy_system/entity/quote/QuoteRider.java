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
public class QuoteRider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quoteRiderId;

    private Long quoteId;

    private Long riderId;

    private BigDecimal riderPremium;

}
