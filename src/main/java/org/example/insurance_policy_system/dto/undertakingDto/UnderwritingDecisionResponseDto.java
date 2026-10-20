package org.example.insurance_policy_system.dto.undertakingDto;

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
    private String exclusionDetails;
    private Long approvedBy;
    private LocalDateTime decisionDate;

}





