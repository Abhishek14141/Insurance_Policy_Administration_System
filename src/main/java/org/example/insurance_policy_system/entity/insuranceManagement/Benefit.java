package org.example.insurance_policy_system.entity.insuranceManagement;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name="benefit_master") @Getter @Setter @NoArgsConstructor
public class Benefit {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="benefit_id") private Long benefitId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id",nullable=false) private InsuranceProduct product;
 @Column(name="benefit_name",nullable=false,length=150) private String benefitName;
 @Column(name="benefit_description",columnDefinition="TEXT") private String benefitDescription;
 @Column(name="benefit_limit",precision=15,scale=2) private BigDecimal benefitLimit;
}
