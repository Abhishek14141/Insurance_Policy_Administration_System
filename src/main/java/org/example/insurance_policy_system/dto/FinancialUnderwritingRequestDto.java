package org.example.insurance_policy_system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialUnderwritingRequestDto {
    @DecimalMin("0.00")
    private BigDecimal annualIncome;
    @DecimalMin("0.00")
    private BigDecimal monthlyIncome;
    private String employerName;
    private String occupation;
    @DecimalMin("0.00")
    private BigDecimal existingInsurance;
    @DecimalMin("0.00")
    private BigDecimal loanLiability;
    private String financialDocumentPath;
}
