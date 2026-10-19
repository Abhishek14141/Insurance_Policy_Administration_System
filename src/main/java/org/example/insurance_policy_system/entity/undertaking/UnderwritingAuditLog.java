package org.example.insurance_policy_system.entity.undertaking;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "underwriting_audit_log")
public class UnderwritingAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Column(name = "action", length = 100)
    private String action;

    @Column(name = "performed_by")
    private Long performedBy;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

}