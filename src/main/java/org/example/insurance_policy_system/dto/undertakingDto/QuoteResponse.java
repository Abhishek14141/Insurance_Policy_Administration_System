package org.example.insurance_policy_system.dto.undertakingDto;

import java.time.LocalDate;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class QuoteResponse {

    private Long quoteId;
    private Long customerId;
    private Long productId;
    private BigDecimal sumInsured;
    private LocalDate quoteExpiryDate;
    private String quoteStatus;
}
