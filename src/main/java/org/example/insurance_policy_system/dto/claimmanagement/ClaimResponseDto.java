package org.example.insurance_policy_system.dto.claimmanagement;

import lombok.Data;
import org.example.insurance_policy_system.enums.claimmanagement.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClaimResponseDto {

    private Long claimId;
    private String claimNumber;
    private Long policyId;
    private Long customerId;
    private String claimType;
    private LocalDate incidentDate;
    private LocalDate claimDate;
   // private String description;
    private BigDecimal estimatedLoss;
    private ClaimStatus claimStatus;
}

