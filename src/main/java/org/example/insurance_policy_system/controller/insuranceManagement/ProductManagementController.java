package org.example.insurance_policy_system.controller.insuranceManagement;

import jakarta.validation.Valid;

import org.example.insurance_policy_system.dto.insuranceManagement.*;
import org.example.insurance_policy_system.entity.insuranceManagement.PremiumFrequency;
import org.example.insurance_policy_system.service.insuranceManagement.ProductManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductManagementController {
 private final ProductManagementService service;
 public ProductManagementController(ProductManagementService service){this.service=service;}

 @PostMapping("/product-categories") public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Category created",service.createCategory(r)));}
 @GetMapping("/product-categories") public ApiResponse<List<CategoryResponse>> categories(){return ApiResponse.ok("Categories retrieved",service.getCategories());}
 @PutMapping("/product-categories/{id}") public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id,@Valid @RequestBody CategoryRequest r){return ApiResponse.ok("Category updated",service.updateCategory(id,r));}
 @DeleteMapping("/product-categories/{id}") public ApiResponse<CategoryResponse> deactivateCategory(@PathVariable Long id){return ApiResponse.ok("Category deactivated",service.deactivateCategory(id));}

 @PostMapping("/products") public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Product created as INACTIVE; configure it before activation",service.createProduct(r)));}
 @GetMapping("/products") public ApiResponse<List<ProductResponse>> products(){return ApiResponse.ok("Products retrieved",service.getProducts());}
 @GetMapping("/products/active") public ApiResponse<List<ProductResponse>> activeProducts(){return ApiResponse.ok("Active products retrieved",service.getActiveProducts());}
 @GetMapping("/products/{id}") public ApiResponse<ProductResponse> product(@PathVariable Long id){return ApiResponse.ok("Product retrieved",service.getProduct(id));}
 @PutMapping("/products/{id}") public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest r){return ApiResponse.ok("Product updated",service.updateProduct(id,r));}
 @PatchMapping("/products/{id}/status") public ApiResponse<ProductResponse> status(@PathVariable Long id,@RequestParam String status){return ApiResponse.ok("Product status updated",service.changeProductStatus(id,status));}
 @DeleteMapping("/products/{id}") public ApiResponse<ProductResponse> deactivateProduct(@PathVariable Long id){return ApiResponse.ok("Product deactivated",service.changeProductStatus(id,"INACTIVE"));}
 @GetMapping("/products/dashboard") public ApiResponse<DashboardResponse> dashboard(){return ApiResponse.ok("Product dashboard retrieved",service.getDashboard());}

 @PostMapping("/products/{id}/coverages") public ResponseEntity<ApiResponse<CoverageResponse>> addCoverage(@PathVariable Long id, @Valid @RequestBody CoverageRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Coverage created",service.addCoverage(id,r)));}
 @GetMapping("/products/{id}/coverages") public ApiResponse<List<CoverageResponse>> coverages(@PathVariable Long id){return ApiResponse.ok("Coverages retrieved",service.getCoverages(id));}
 @PutMapping("/coverages/{coverageId}") public ApiResponse<CoverageResponse> updateCoverage(@PathVariable Long coverageId,@Valid @RequestBody CoverageRequest r){return ApiResponse.ok("Coverage updated",service.updateCoverage(coverageId,r));}
 @DeleteMapping("/coverages/{coverageId}") public ApiResponse<Void> deleteCoverage(@PathVariable Long coverageId){service.deleteCoverage(coverageId);return ApiResponse.ok("Coverage deleted",null);}

 @PostMapping("/products/{id}/riders") public ResponseEntity<ApiResponse<RiderResponse>> addRider(@PathVariable Long id,@Valid @RequestBody RiderRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Rider created",service.addRider(id,r)));}
 @GetMapping("/products/{id}/riders") public ApiResponse<List<RiderResponse>> riders(@PathVariable Long id){return ApiResponse.ok("Riders retrieved",service.getRiders(id));}
 @PutMapping("/riders/{riderId}") public ApiResponse<RiderResponse> updateRider(@PathVariable Long riderId,@Valid @RequestBody RiderRequest r){return ApiResponse.ok("Rider updated",service.updateRider(riderId,r));}
 @DeleteMapping("/riders/{riderId}") public ApiResponse<Void> deleteRider(@PathVariable Long riderId){service.deleteRider(riderId);return ApiResponse.ok("Rider deactivated",null);}

 @PostMapping("/products/{id}/benefits") public ResponseEntity<ApiResponse<BenefitResponse>> addBenefit(@PathVariable Long id,@Valid @RequestBody BenefitRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Benefit created",service.addBenefit(id,r)));}
 @GetMapping("/products/{id}/benefits") public ApiResponse<List<BenefitResponse>> benefits(@PathVariable Long id){return ApiResponse.ok("Benefits retrieved",service.getBenefits(id));}
 @PutMapping("/benefits/{benefitId}") public ApiResponse<BenefitResponse> updateBenefit(@PathVariable Long benefitId,@Valid @RequestBody BenefitRequest r){return ApiResponse.ok("Benefit updated",service.updateBenefit(benefitId,r));}
 @DeleteMapping("/benefits/{benefitId}") public ApiResponse<Void> deleteBenefit(@PathVariable Long benefitId){service.deleteBenefit(benefitId);return ApiResponse.ok("Benefit deleted",null);}

 @PostMapping("/premium-frequencies") public ResponseEntity<ApiResponse<PremiumFrequency>> createFrequency(@Valid @RequestBody FrequencyRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Premium frequency created",service.createFrequency(r)));}
 @GetMapping("/premium-frequencies") public ApiResponse<List<PremiumFrequency>> frequencies(){return ApiResponse.ok("Premium frequencies retrieved",service.getFrequencies());}
 @PutMapping("/premium-frequencies/{id}") public ApiResponse<PremiumFrequency> updateFrequency(@PathVariable Long id, @Valid @RequestBody FrequencyRequest r){return ApiResponse.ok("Premium frequency updated",service.updateFrequency(id,r));}

 @PostMapping("/products/{id}/commission") public ResponseEntity<ApiResponse<CommissionResponse>> saveCommission(@PathVariable Long id,@Valid @RequestBody CommissionRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Commission configuration saved",service.saveCommission(id,r)));}
 @GetMapping("/products/{id}/commission") public ApiResponse<CommissionResponse> commission(@PathVariable Long id){return ApiResponse.ok("Commission configuration retrieved",service.getCommission(id));}
 @PutMapping("/products/{id}/commission") public ApiResponse<CommissionResponse> updateCommission(@PathVariable Long id,@Valid @RequestBody CommissionRequest r){return ApiResponse.ok("Commission configuration saved",service.saveCommission(id,r));}
}
