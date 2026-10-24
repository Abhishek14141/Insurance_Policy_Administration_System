package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "underwriting_audit_log")
@Data
public class UnderwritingAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @ManyToOne
    @JoinColumn(name = "underwriting_id", nullable = false)
    private UnderwritingCase underwritingCase;

    @Column(nullable = false)
    private String action;

    private Long performedBy;

    @Column(length = 1000)
    private String remarks;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;
}
