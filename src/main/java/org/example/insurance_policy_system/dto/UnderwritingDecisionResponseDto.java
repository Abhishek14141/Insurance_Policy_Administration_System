package org.example.insurance_policy_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.UnderwritingDecisionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecisionResponseDto {
    private Long decisionId;
    private Long underwritingId;
    private UnderwritingDecisionStatus decision;
    private BigDecimal loadingPercentage;
    private Long approvedBy;
    private String rejectionReason;
    private LocalDateTime decisionDate;

}





