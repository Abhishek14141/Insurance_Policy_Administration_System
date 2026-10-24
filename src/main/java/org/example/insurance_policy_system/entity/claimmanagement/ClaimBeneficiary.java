package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "claim_beneficiary",
       uniqueConstraints = @UniqueConstraint(columnNames = "claim_id"))
public class ClaimBeneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long beneficiaryId;

    @Column(name = "claim_id", nullable = false)
    private Long claimId;

    @Column(nullable = false)
    private String accountHolderName;

    @Column(nullable = false)
    private String accountNumber; // Demo only; encrypt for production

    @Column(nullable = false)
    private String ifscCode;

    @Column(nullable = false)
    private String bankName;

    private String razorpayContactId;

    private String razorpayFundAccountId;

    @Column(nullable = false)
    private String verificationStatus = "PENDING";
}