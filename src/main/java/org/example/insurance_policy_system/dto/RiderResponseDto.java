package org.example.insurance_policy_system.dto;
import lombok.Data; import java.math.BigDecimal;
@Data public class RiderResponseDto {
 private Long riderId;
 private Long productId;
 private String riderName;
 private String riderDescription;
 private BigDecimal riderPremium;
 private String status;
}
