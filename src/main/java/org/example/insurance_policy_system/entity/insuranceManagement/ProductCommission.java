package org.example.insurance_policy_system.entity.insuranceManagement;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name="product_commission") @Getter @Setter @NoArgsConstructor
public class ProductCommission {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="commission_id") private Long commissionId;
 @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id",nullable=false,unique=true) private InsuranceProduct product;
 @Column(name="first_year_commission",nullable=false,precision=5,scale=2) private BigDecimal firstYearCommission=BigDecimal.ZERO;
 @Column(name="renewal_commission",nullable=false,precision=5,scale=2) private BigDecimal renewalCommission=BigDecimal.ZERO;
 @Column(name="branch_commission",nullable=false,precision=5,scale=2) private BigDecimal branchCommission=BigDecimal.ZERO;
}
