package org.example.insurance_policy_system.entity.insuranceManagement;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="insurance_product")
@Getter @Setter @NoArgsConstructor
public class InsuranceProduct {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="product_id") private Long productId;
 @Column(name="product_code",nullable=false,unique=true,length=30) private String productCode;
 @Column(name="product_name",nullable=false,unique=true,length=150) private String productName;
 @Column(name="product_type",nullable=false,length=100) private String productType;
 @Column(columnDefinition="TEXT") private String description;
 @Column(name="minimum_age") private Integer minimumAge;
 @Column(name="maximum_age") private Integer maximumAge;
 @Column(name="minimum_sum_insured",precision=15,scale=2) private BigDecimal minimumSumInsured;
 @Column(name="maximum_sum_insured",precision=15,scale=2) private BigDecimal maximumSumInsured;
 @Column(nullable=false,length=20) private String status="INACTIVE";
 @Column(name="created_at",insertable=false,updatable=false) private LocalDateTime createdAt;
 @Column(name="updated_at",insertable=false,updatable=false) private LocalDateTime updatedAt;
}
