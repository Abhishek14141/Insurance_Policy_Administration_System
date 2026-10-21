package org.example.insurance_policy_system.dto.claimsettlement;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClaimResponseDto {

   private Long claimId;
 private    String status;
 private    BigDecimal approvedAmount;


}
