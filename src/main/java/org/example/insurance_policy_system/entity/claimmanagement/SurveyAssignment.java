package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.claimmanagement.SurveyStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long surveyId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
    private ClaimMaster claim;

    @Column(name = "surveyor_id")
    private Long surveyorId;


    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;


    @Column(name = "inspection_date")
    private LocalDateTime inspectionDate;

    @Column(name = "report_path", length = 500)
    private String reportPath;


    @Enumerated(EnumType.STRING)
    @Column(name = "survey_status")
    private SurveyStatus surveyStatus;


    @PrePersist
    public void prePersist() {
        if (assignedDate == null) {
            assignedDate = LocalDateTime.now();
        }


        if (surveyStatus == null) {
            surveyStatus = SurveyStatus.ASSIGNED;
        }
    }
}
