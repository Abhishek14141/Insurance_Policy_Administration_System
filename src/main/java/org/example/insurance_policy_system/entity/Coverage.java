package org.example.insurance_policy_system.entity;
import jakarta.persistence.*; import lombok.Data; import java.math.BigDecimal;
@Entity @Table(name="coverage_master") @Data public class Coverage {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="coverage_id") private Long coverageId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id",nullable=false) private InsuranceProduct product;
 @Column(name="coverage_name",nullable=false,length=150) private String coverageName;
 @Column(name="minimum_coverage",precision=15,scale=2) private BigDecimal minimumCoverage;
 @Column(name="maximum_coverage",precision=15,scale=2) private BigDecimal maximumCoverage;
 @Column(name="waiting_period_days") private Integer waitingPeriodDays;
 @Column(name="deductible_amount",precision=15,scale=2) private BigDecimal deductibleAmount;
}
