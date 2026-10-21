package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.claimmanagement.SettlementStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "claim_settlement")
public class ClaimSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "settlement_id")
    private  Long settlementId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
private ClaimMaster claimId;

    @Column(name = "approved_amount" , precision = 15, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "payment_mode", length = 50)
private  String paymentMode;
    @Column(name = "transaction_reference", length = 100)
private  String  transactionReference;

    @Column(name = "settlement_date")
private LocalDateTime settlementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status")
private SettlementStatus settlementStatus;





}
