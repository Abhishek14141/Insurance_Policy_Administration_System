package org.example.insurance_policy_system.dto.insuranceManagement;
import java.util.Map;
public record DashboardResponse(long totalProducts,long activeProducts,long inactiveProducts,long productsWithRiders,Map<String,Long> productsByCategory) {}
