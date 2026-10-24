package org.example.insurance_policy_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PolicyUnderwritingDetailsDto {

    private Long underwritingId;
    private Long quoteId;
    private Long customerId;
    private Long productId;
    private String underwritingStatus;
    private String decision;
    private Integer totalScore;
    private String riskCategory;
    private BigDecimal loadingPercentage;
    private Long approvedBy;
    private LocalDateTime decisionDate;
}