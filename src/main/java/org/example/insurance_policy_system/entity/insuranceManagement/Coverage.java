package org.example.insurance_policy_system.entity.insuranceManagement;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name="coverage_master") @Getter @Setter @NoArgsConstructor
public class Coverage {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="coverage_id") private Long coverageId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id",nullable=false) private InsuranceProduct product;
 @Column(name="coverage_name",nullable=false,length=150) private String coverageName;
 @Column(name="minimum_coverage",nullable=false,precision=15,scale=2) private BigDecimal minimumCoverage;
 @Column(name="maximum_coverage",nullable=false,precision=15,scale=2) private BigDecimal maximumCoverage;
 @Column(name="waiting_period_days",nullable=false) private Integer waitingPeriodDays=0;
 @Column(name="deductible_amount",nullable=false,precision=15,scale=2) private BigDecimal deductibleAmount=BigDecimal.ZERO;
}
