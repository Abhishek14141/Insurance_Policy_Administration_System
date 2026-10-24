package org.example.insurance_policy_system.entity;
import jakarta.persistence.*; import lombok.Data; import org.example.insurance_policy_system.enums.CategoryStatus;
@Entity @Table(name="product_category") @Data public class ProductCategory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="category_id") private Long categoryId;
 @Column(name="category_name",nullable=false,unique=true,length=100) private String categoryName;
 @Column(length=255) private String description;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private CategoryStatus status=CategoryStatus.ACTIVE;
}
