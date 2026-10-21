package org.example.insurance_policy_system.entity.undertaking;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_underwiritng")
@Data
public class FinancialUnderwriting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "financial_id")
    private Long financialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Column(name = "annual_income", precision = 15, scale = 2)
    private BigDecimal annualIncome;

    @Column(name = "employer_name", length = 150)
    private String employerName;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "existing_insurance", precision = 15, scale = 2)
    private BigDecimal existingInsurance;

    @Column(name = "loan_liability", precision = 15, scale = 2)
    private BigDecimal loanLiability;


}