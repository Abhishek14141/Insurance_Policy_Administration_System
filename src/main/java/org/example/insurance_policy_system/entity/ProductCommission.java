package org.example.insurance_policy_system.entity;
import jakarta.persistence.*; import lombok.Data; import java.math.BigDecimal;
@Entity @Table(name="product_commission") @Data public class ProductCommission {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="commission_id") private Long commissionId;
 @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id",nullable=false,unique=true) private InsuranceProduct product;
 @Column(name="first_year_commission",precision=5,scale=2) private BigDecimal firstYearCommission;
 @Column(name="renewal_commission",precision=5,scale=2) private BigDecimal renewalCommission;
 @Column(name="branch_commission",precision=5,scale=2) private BigDecimal branchCommission;
}
