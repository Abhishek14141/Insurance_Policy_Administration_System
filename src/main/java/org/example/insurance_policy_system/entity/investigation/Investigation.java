package org.example.insurance_policy_system.entity.investigation;

import org.example.insurance_policy_system.enums.investigation.InvestigationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "investigation")
@Getter
@Setter
@NoArgsConstructor
public class Investigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investigation_id")
    private Long investigationId;

    @Column(name = "claim_id", nullable = false)
    private Long claimId;

    @Column(name = "investigator_id", nullable = false)
    private Long investigatorId;

    @Column(name = "investigation_reason", columnDefinition = "TEXT")
    private String investigationReason;

    @Column(name = "report_path", length = 500)
    private String reportPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "investigation_status", nullable = false)
    private InvestigationStatus investigationStatus =
            InvestigationStatus.Pending;

}
