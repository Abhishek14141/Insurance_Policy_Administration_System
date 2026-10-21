package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
    private ClaimMaster claim;

    @Column(name = "action", length = 100)
    private String action;

    @Column(name = "performed_by")
    private Long performedBy;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;



}
