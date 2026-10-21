package org.example.insurance_policy_system.service.insuranceManagement;


import org.example.insurance_policy_system.dto.insuranceManagement.*;
import org.example.insurance_policy_system.entity.insuranceManagement.PremiumFrequency;

import java.util.List;
public interface ProductManagementService {
 CategoryResponse createCategory(CategoryRequest request);
 List<CategoryResponse> getCategories();
 CategoryResponse updateCategory(Long id, CategoryRequest request);
 CategoryResponse deactivateCategory(Long id);
 ProductResponse createProduct(ProductRequest request);
 List<ProductResponse> getProducts();
 List<ProductResponse> getActiveProducts();
 ProductResponse getProduct(Long id);
 ProductResponse updateProduct(Long id, ProductRequest request);
 ProductResponse changeProductStatus(Long id,String status);
 DashboardResponse getDashboard();
 CoverageResponse addCoverage(Long productId, CoverageRequest request);
 List<CoverageResponse> getCoverages(Long productId);
 CoverageResponse updateCoverage(Long id,CoverageRequest request);
 void deleteCoverage(Long id);
 RiderResponse addRider(Long productId, RiderRequest request);
 List<RiderResponse> getRiders(Long productId);
 RiderResponse updateRider(Long id,RiderRequest request);
 void deleteRider(Long id);
 BenefitResponse addBenefit(Long productId,BenefitRequest request);
 List<BenefitResponse> getBenefits(Long productId);
 BenefitResponse updateBenefit(Long id,BenefitRequest request);
 void deleteBenefit(Long id);
 PremiumFrequency createFrequency(FrequencyRequest request);
 List<PremiumFrequency> getFrequencies();
 PremiumFrequency updateFrequency(Long id,FrequencyRequest request);
 CommissionResponse saveCommission(Long productId,CommissionRequest request);
 CommissionResponse getCommission(Long productId);
}
