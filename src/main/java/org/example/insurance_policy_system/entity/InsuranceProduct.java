package org.example.insurance_policy_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.insurance_policy_system.enums.ProductStatus;
import org.example.insurance_policy_system.enums.ProductType;
import java.math.BigDecimal;

@Entity
@Table(name = "insurance_product")
@Data
public class InsuranceProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") private Long productId;
    @Column(name = "product_code", nullable = false, unique = true, length = 30) private String productCode;
    @Column(name = "product_name", nullable = false, length = 150) private String productName;
    @Enumerated(EnumType.STRING) @Column(name = "product_type", nullable = false, length = 20) private ProductType productType;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "category_id", nullable = false) private ProductCategory category;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "minimum_age") private Integer minimumAge;
    @Column(name = "maximum_age") private Integer maximumAge;
    @Column(name = "minimum_sum_insured", precision = 15, scale = 2) private BigDecimal minSumInsured;
    @Column(name = "maximum_sum_insured", precision = 15, scale = 2) private BigDecimal maxSumInsured;
    @Column(name = "min_term_years") private Integer minTermYears;
    @Column(name = "max_term_years") private Integer maxTermYears;
    @Column(name = "product_rate", precision = 8, scale = 3) private BigDecimal productRate = new BigDecimal("2.500");
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private ProductStatus status = ProductStatus.INACTIVE;
}
