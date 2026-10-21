package org.example.insurance_policy_system.dto.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.claimmanagement.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimSettlementResponseDto {


    private  Long settlementId;


    private Long claimId;

    private BigDecimal approvedAmount;

    private  String paymentMode;
    private  String  transactionReference;

    private LocalDateTime settlementDate;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus;






}
