package org.example.insurance_policy_system.dto.insuranceManagement;
import java.math.BigDecimal;
public record RiderResponse(Long riderId,Long productId,String riderName,String riderDescription,BigDecimal riderPremium,String status) {}
