
package org.example.insurance_policy_system.entity.kyc;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kyc_verification_log")
@Getter
@Setter
public class KycAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "kyc_id", nullable = false)
    private KycRequest kyc;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "performed_by")
    private Long performedBy;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

    @Column(length = 500)
    private String remarks;

    @PrePersist
    void onCreate() {
        if (actionDate == null) {
            actionDate = LocalDateTime.now();
        }
    }
}
