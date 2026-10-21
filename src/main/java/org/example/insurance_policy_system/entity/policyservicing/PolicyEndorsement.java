package org.example.insurance_policy_system.entity.policyservicing;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.insurance_policy_system.enums.policyservicing.EndorsementApprovalStatus;

@Entity
@Table(
        name = "policy_endorsement",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_endorsement_number",
                        columnNames = "endorsement_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEndorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endorsement_id")
    private Long endorsementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(name = "endorsement_number", nullable = false, unique = true)
    private String endorsementNumber;

    @Column(name = "endorsement_type", nullable = false)
    private String endorsementType;

    @Column(name = "previous_value", columnDefinition = "TEXT")
    private String previousValue;

    @Column(name = "updated_value", columnDefinition = "TEXT")
    private String updatedValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private EndorsementApprovalStatus approvalStatus =
            EndorsementApprovalStatus.PENDING;
}
