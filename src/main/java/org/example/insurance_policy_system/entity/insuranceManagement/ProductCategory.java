package org.example.insurance_policy_system.entity.insuranceManagement;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="product_category")
@Getter @Setter @NoArgsConstructor
public class ProductCategory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="category_id") private Long categoryId;
 @Column(name="category_name",nullable=false,unique=true,length=100) private String categoryName;
 @Column(length=255) private String description;
 @Column(nullable=false,length=20) private String status="ACTIVE";
 @Column(name="created_at",insertable=false,updatable=false) private LocalDateTime createdAt;
 @Column(name="updated_at",insertable=false,updatable=false) private LocalDateTime updatedAt;
}
