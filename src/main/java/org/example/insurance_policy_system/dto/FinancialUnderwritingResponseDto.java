package org.example.insurance_policy_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialUnderwritingResponseDto {

    private Long financialId;
    private Long underwritingId;
    private BigDecimal annualIncome;
    private String employerName;
    private BigDecimal monthlyIncome;
    private String occupation;
    private BigDecimal existingInsurance;
    private BigDecimal loanLiability;
    private String financialDocumentPath;

}
