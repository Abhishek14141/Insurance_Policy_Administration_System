package org.example.insurance_policy_system.entity.policyservicing;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_request_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(name = "action", nullable = false)
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_by")
    private Employee actionBy;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
