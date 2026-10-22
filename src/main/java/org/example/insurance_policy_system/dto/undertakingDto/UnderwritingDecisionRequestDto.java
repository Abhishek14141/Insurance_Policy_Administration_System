package org.example.insurance_policy_system.dto.undertakingDto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.UnderwritingDecisionStatus;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecisionRequestDto {
    @NotNull
    private UnderwritingDecisionStatus decision;
    @NotNull
    @Positive
    private Long approvedBy;

    private String rejectionReason;

}
