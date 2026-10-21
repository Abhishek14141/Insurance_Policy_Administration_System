package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.claimmanagement.InvestigationStatus;

@Entity
@Table(name = "investigation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Investigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investigation_id")
    private Long investigationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
    private ClaimMaster claim;

    @Column(name = "investigator_id")
    private Long investigatorId;

    @Column(name = "investigation_reason", columnDefinition = "TEXT")
    private String investigationReason;


    @Column(name = "report_path", length = 500)
    private String reportPath;


    @Enumerated(EnumType.STRING)
    @Column(name = "investigation_status")
    private InvestigationStatus investigationStatus;

}
