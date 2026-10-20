package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialUnderwritingRequestDto {

    private BigDecimal annualIncome;
    private String employerName;
    private String occupation;
    private BigDecimal existingInsurance;
    private BigDecimal loanLiability;
}
