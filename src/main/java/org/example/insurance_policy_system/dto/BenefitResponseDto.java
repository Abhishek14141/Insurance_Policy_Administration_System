package org.example.insurance_policy_system.dto;
import lombok.Data; import java.math.BigDecimal;
@Data public class BenefitResponseDto {
 private Long benefitId;
 private Long productId;
 private String benefitName;
 private String benefitDescription;
 private BigDecimal benefitLimit;
 private String status;
}
