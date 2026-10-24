package org.example.insurance_policy_system.entity;
import jakarta.persistence.*; import lombok.Data; import java.time.LocalDateTime;
@Entity @Table(name="quote_history") @Data public class QuoteHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="history_id") private Long historyId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="quote_id",nullable=false) private Quote quote;
 @Column(nullable=false,length=100) private String action; @Column(name="performed_by") private Long performedBy;
 @Column(name="action_date",nullable=false) private LocalDateTime actionDate; @Column(length=500) private String remarks;
}
