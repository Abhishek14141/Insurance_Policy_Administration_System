package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.claimmanagement.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "claim_master")
public class ClaimMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long claimId;

    @Column(name = "claim_number", length = 30, unique = true)
    private String claimNumber;

    @Column(name = "policy_id")
    private Long policyId;


    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "claim_type", length = 50)
    private String claimType;

    @Column(name = "incident_date")
    private LocalDate incidentDate;

    @Column(name = "claim_date")
    private LocalDate claimDate;



    @Column(name = "estimated_loss", precision = 15, scale = 2)
    private BigDecimal estimatedLoss;


    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status")
    private ClaimStatus claimStatus;
}
