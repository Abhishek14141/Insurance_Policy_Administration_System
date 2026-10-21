package org.example.insurance_policy_system.entity.quote;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.quote.QuoteStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long quoteId;

    @NotBlank(message = "Quote number can't be empty")
    private String quoteNumber;
    private Long customerId;

    private Long productId;

    private Long policyTerm;

    private BigDecimal sumInsured;

    private Long premiumFrequency;

    private LocalDate quoteDate;

    private LocalDate quoteExpiryDate;

    private QuoteStatus quoteStatus;


}
