package org.example.insurance_policy_system.serviceimpl.insuranceManagement;
import org.example.insurance_policy_system.dto.insuranceManagement.ProductRequest;
import org.example.insurance_policy_system.dto.insuranceManagement.*;
import org.example.insurance_policy_system.entity.insuranceManagement.*;
import org.example.insurance_policy_system.enums.insuranceManagement.PremiumFrequencyStatus;
import org.example.insurance_policy_system.enums.insuranceManagement.ProductStatus;
import org.example.insurance_policy_system.enums.insuranceManagement.ProductType;
import org.example.insurance_policy_system.enums.insuranceManagement.RiderStatus;
import org.example.insurance_policy_system.exception.insuranceManagement.BadRequestException;
import org.example.insurance_policy_system.exception.insuranceManagement.ResourceNotFoundException;
import org.example.insurance_policy_system.repository.insuranceManagement.*;
import org.example.insurance_policy_system.service.insuranceManagement.ProductManagementService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductManagementServiceImpl implements ProductManagementService {

 private final ProductCategoryRepository categoryRepo;
 private final InsuranceProductRepository productRepo;
 private final CoverageRepository coverageRepo;
 private final RiderRepository riderRepo;
 private final BenefitRepository benefitRepo;
 private final PremiumFrequencyRepository frequencyRepo;
 private final ProductCommissionRepository commissionRepo;

 public ProductManagementServiceImpl(
         ProductCategoryRepository categoryRepo,
         InsuranceProductRepository productRepo,
         CoverageRepository coverageRepo,
         RiderRepository riderRepo,
         BenefitRepository benefitRepo,
         PremiumFrequencyRepository frequencyRepo,
         ProductCommissionRepository commissionRepo) {

  this.categoryRepo = categoryRepo;
  this.productRepo = productRepo;
  this.coverageRepo = coverageRepo;
  this.riderRepo = riderRepo;
  this.benefitRepo = benefitRepo;
  this.frequencyRepo = frequencyRepo;
  this.commissionRepo = commissionRepo;
 }

 // ==================== FIND HELPERS ====================

 private ProductCategory category(Long id) {
  return categoryRepo.findById(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Category not found: " + id));
 }

 private InsuranceProduct product(Long id) {
  return productRepo.findById(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Product not found: " + id));
 }

 private Coverage coverage(Long id) {
  return coverageRepo.findById(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Coverage not found: " + id));
 }

 private Rider rider(Long id) {
  return riderRepo.findById(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Rider not found: " + id));
 }

 private Benefit benefit(Long id) {
  return benefitRepo.findById(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Benefit not found: " + id));
 }

 // ==================== VALIDATION ====================

 private void validateRange(
         Integer minAge,
         Integer maxAge,
         BigDecimal minSum,
         BigDecimal maxSum,
         Integer minTerm,
         Integer maxTerm) {

  if (minAge != null && minAge < 0) {
   throw new BadRequestException(
           "Minimum age cannot be negative.");
  }

  if (maxAge != null && maxAge < 0) {
   throw new BadRequestException(
           "Maximum age cannot be negative.");
  }

  if (minAge != null && maxAge != null && minAge > maxAge) {
   throw new BadRequestException(
           "Minimum age cannot exceed maximum age.");
  }

  if (minSum != null &&
          minSum.compareTo(BigDecimal.ZERO) < 0) {
   throw new BadRequestException(
           "Minimum sum insured cannot be negative.");
  }

  if (maxSum != null &&
          maxSum.compareTo(BigDecimal.ZERO) < 0) {
   throw new BadRequestException(
           "Maximum sum insured cannot be negative.");
  }

  if (minSum != null && maxSum != null
          && minSum.compareTo(maxSum) > 0) {
   throw new BadRequestException(
           "Minimum sum insured cannot exceed maximum sum insured.");
  }

  if (minTerm != null && minTerm <= 0) {
   throw new BadRequestException(
           "Minimum policy term must be greater than zero.");
  }

  if (maxTerm != null && maxTerm <= 0) {
   throw new BadRequestException(
           "Maximum policy term must be greater than zero.");
  }

  if (minTerm != null && maxTerm != null && minTerm > maxTerm) {
   throw new BadRequestException(
           "Minimum policy term cannot exceed maximum policy term.");
  }
 }

 private void validateCoverage(CoverageRequest r) {
  if (r.minimumCoverage() == null ||
          r.maximumCoverage() == null) {
   throw new BadRequestException(
           "Minimum and maximum coverage are required.");
  }

  if (r.minimumCoverage().compareTo(BigDecimal.ZERO) < 0 ||
          r.maximumCoverage().compareTo(BigDecimal.ZERO) < 0) {
   throw new BadRequestException(
           "Coverage amounts cannot be negative.");
  }

  if (r.minimumCoverage().compareTo(r.maximumCoverage()) > 0) {
   throw new BadRequestException(
           "Minimum coverage cannot exceed maximum coverage.");
  }

  if (r.waitingPeriodDays() != null &&
          r.waitingPeriodDays() < 0) {
   throw new BadRequestException(
           "Waiting period cannot be negative.");
  }

  if (r.deductibleAmount() != null &&
          r.deductibleAmount().compareTo(BigDecimal.ZERO) < 0) {
   throw new BadRequestException(
           "Deductible amount cannot be negative.");
  }
 }

 private String normalizeStatus(String status, String fallback) {
  String normalized = (status == null || status.isBlank())
          ? fallback
          : status.trim().toUpperCase(Locale.ROOT);

  if (!Set.of("ACTIVE", "INACTIVE").contains(normalized)) {
   throw new BadRequestException(
           "Status must be ACTIVE or INACTIVE.");
  }

  return normalized;
 }

 private RiderStatus normalizeRiderStatus(RiderStatus status) {
  return status == null ? RiderStatus.ACTIVE : status;
 }

 // ==================== RESPONSE MAPPERS ====================

 private CategoryResponse catResponse(ProductCategory c) {
  return new CategoryResponse(
          c.getCategoryId(),
          c.getCategoryName(),
          c.getDescription(),
          c.getStatus());
 }

 private ProductResponse productResponse(InsuranceProduct p) {
  return new ProductResponse(
          p.getProductId(),
          p.getProductCode(),
          p.getProductName(),
          p.getProductType(),
          p.getDescription(),
          p.getCategory().getCategoryId(),
          p.getCategory().getCategoryName(),
          p.getMinimumAge(),
          p.getMaximumAge(),
          p.getMinimumSumInsured(),
          p.getMaximumSumInsured(),
          p.getMinimumPolicyTermYears(),
          p.getMaximumPolicyTermYears(),
          p.getStatus());
 }

 private CoverageResponse coverageResponse(Coverage c) {
  return new CoverageResponse(
          c.getCoverageId(),
          c.getProduct().getProductId(),
          c.getCoverageName(),
          c.getMinimumCoverage(),
          c.getMaximumCoverage(),
          c.getWaitingPeriodDays(),
          c.getDeductibleAmount());
 }

 private RiderResponse riderResponse(Rider r) {
  return new RiderResponse(
          r.getRiderId(),
          r.getProduct().getProductId(),
          r.getRiderName(),
          r.getRiderDescription(),
          r.getRiderPremium(),
          r.getStatus());
 }

 private BenefitResponse benefitResponse(Benefit b) {
  return new BenefitResponse(
          b.getBenefitId(),
          b.getProduct().getProductId(),
          b.getBenefitName(),
          b.getBenefitDescription(),
          b.getBenefitLimit());
 }

 private CommissionResponse commissionResponse(ProductCommission c) {
  return new CommissionResponse(
          c.getCommissionId(),
          c.getProduct().getProductId(),
          c.getFirstYearCommission(),
          c.getRenewalCommission(),
          c.getBranchCommission());
 }

 // ==================== CATEGORY APIs ====================

 @Override
 public CategoryResponse createCategory(CategoryRequest r) {
  String name = r.categoryName().trim();

  if (categoryRepo.existsByCategoryNameIgnoreCase(name)) {
   throw new BadRequestException(
           "Category name already exists.");
  }

  ProductCategory c = new ProductCategory();
  c.setCategoryName(name);
  c.setDescription(r.description());
  c.setStatus("ACTIVE");

  return catResponse(categoryRepo.save(c));
 }

 @Override
 @Transactional(readOnly = true)
 public List<CategoryResponse> getCategories() {
  return categoryRepo.findAll()
          .stream()
          .map(this::catResponse)
          .toList();
 }

 @Override
 public CategoryResponse updateCategory(Long id, CategoryRequest r) {
  ProductCategory c = category(id);
  String name = r.categoryName().trim();

  boolean duplicate = categoryRepo.findAll()
          .stream()
          .anyMatch(existing ->
                  !existing.getCategoryId().equals(id)
                          && existing.getCategoryName()
                          .equalsIgnoreCase(name));

  if (duplicate) {
   throw new BadRequestException(
           "Category name already exists.");
  }

  c.setCategoryName(name);
  c.setDescription(r.description());

  return catResponse(categoryRepo.save(c));
 }

 @Override
 public CategoryResponse deactivateCategory(Long id) {
  ProductCategory c = category(id);

  if (productRepo.countByCategory_CategoryId(id) > 0) {
   throw new BadRequestException(
           "Category has products; deactivate the products or move them first.");
  }

  c.setStatus("INACTIVE");
  return catResponse(categoryRepo.save(c));
 }

 // ==================== PRODUCT APIs ====================

 @Override
 public ProductResponse createProduct(ProductRequest r) {
  validateRange(
          r.minimumAge(),
          r.maximumAge(),
          r.minimumSumInsured(),
          r.maximumSumInsured(),
          r.minimumPolicyTermYears(),
          r.maximumPolicyTermYears());

  String name = r.productName().trim();

  if (productRepo.existsByProductNameIgnoreCase(name)) {
   throw new BadRequestException(
           "Product name already exists.");
  }

  ProductCategory c = category(r.categoryId());

  if (!"ACTIVE".equalsIgnoreCase(c.getStatus())) {
   throw new BadRequestException(
           "Cannot create product under an inactive category.");
  }

  InsuranceProduct p = new InsuranceProduct();
  p.setCategory(c);
  p.setProductName(name);
  p.setProductType(ProductType.valueOf(r.productType().trim().toUpperCase()));  p.setDescription(r.description());
  p.setMinimumAge(r.minimumAge());
  p.setMaximumAge(r.maximumAge());
  p.setMinimumSumInsured(r.minimumSumInsured());
  p.setMaximumSumInsured(r.maximumSumInsured());
  p.setMinimumPolicyTermYears(r.minimumPolicyTermYears());
  p.setMaximumPolicyTermYears(r.maximumPolicyTermYears());

  p.setProductCode(
          "IP-" + UUID.randomUUID()
                  .toString()
                  .replace("-", "")
                  .substring(0, 12)
                  .toUpperCase(Locale.ROOT));

  p.setStatus(ProductStatus.INACTIVE);
  return productResponse(productRepo.save(p));
 }

 @Override
 @Transactional(readOnly = true)
 public List<ProductResponse> getProducts() {
  return productRepo.findAll()
          .stream()
          .map(this::productResponse)
          .toList();
 }

 @Override
 @Transactional(readOnly = true)
 public List<ProductResponse> getActiveProducts() {
  return productRepo.findByStatusIgnoreCase("ACTIVE")
          .stream()
          .map(this::productResponse)
          .toList();
 }

 @Override
 @Transactional(readOnly = true)
 public ProductResponse getProduct(Long id) {
  return productResponse(product(id));
 }

 @Override
 public ProductResponse updateProduct(Long id, ProductRequest r) {
  validateRange(
          r.minimumAge(),
          r.maximumAge(),
          r.minimumSumInsured(),
          r.maximumSumInsured(),
          r.minimumPolicyTermYears(),
          r.maximumPolicyTermYears());

  InsuranceProduct p = product(id);
  String name = r.productName().trim();

  if (productRepo.existsByProductNameIgnoreCaseAndProductIdNot(
          name, id)) {
   throw new BadRequestException(
           "Product name already exists.");
  }

  ProductCategory c = category(r.categoryId());

  if (!"ACTIVE".equalsIgnoreCase(c.getStatus())) {
   throw new BadRequestException(
           "Cannot assign an inactive category.");
  }

  p.setCategory(c);
  p.setProductName(name);
  p.setProductType(ProductType.valueOf(r.productType().trim().toUpperCase()));  p.setDescription(r.description());
  p.setMinimumAge(r.minimumAge());
  p.setMaximumAge(r.maximumAge());
  p.setMinimumSumInsured(r.minimumSumInsured());
  p.setMaximumSumInsured(r.maximumSumInsured());
  p.setMinimumPolicyTermYears(r.minimumPolicyTermYears());
  p.setMaximumPolicyTermYears(r.maximumPolicyTermYears());

  return productResponse(productRepo.save(p));
 }

 @Override
 public ProductResponse changeProductStatus(Long id, String status) {
  InsuranceProduct p = product(id);

  String normalized = status == null
          ? ""
          : status.trim().toUpperCase(Locale.ROOT);

  if (!Set.of("ACTIVE", "INACTIVE").contains(normalized)) {
   throw new BadRequestException(
           "Status must be ACTIVE or INACTIVE.");
  }

  if ("ACTIVE".equals(normalized)) {
   if (p.getMinimumSumInsured() == null ||
           p.getMaximumSumInsured() == null) {
    throw new BadRequestException(
            "Configure minimum and maximum sum insured before activating a product.");
   }

   if (p.getMinimumPolicyTermYears() == null ||
           p.getMaximumPolicyTermYears() == null) {
    throw new BadRequestException(
            "Configure minimum and maximum policy terms before activating a product.");
   }
  }

  p.setStatus(
          normalized == null || normalized.isBlank()
                  ? ProductStatus.INACTIVE
                  : ProductStatus.valueOf(
                  normalized.trim().toUpperCase(java.util.Locale.ROOT)
          )
  );  return productResponse(productRepo.save(p));
 }

 @Override
 @Transactional(readOnly = true)
 public DashboardResponse getDashboard() {
  List<InsuranceProduct> all = productRepo.findAll();

  Map<String, Long> byCategory = all.stream()
          .collect(Collectors.groupingBy(
                  p -> p.getCategory().getCategoryName(),
                  TreeMap::new,
                  Collectors.counting()));

  long withRiders = all.stream()
          .filter(p ->
                  !riderRepo.findByProduct_ProductId(
                          p.getProductId()).isEmpty())
          .count();

  return new DashboardResponse(
          all.size(),
          productRepo.countByStatusIgnoreCase("ACTIVE"),
          productRepo.countByStatusIgnoreCase("INACTIVE"),
          withRiders,
          byCategory);
 }

 // ==================== COVERAGE APIs ====================

 @Override
 public CoverageResponse addCoverage(Long productId, CoverageRequest r) {
  InsuranceProduct p = product(productId);
  validateCoverage(r);

  Coverage c = new Coverage();
  c.setProduct(p);
  c.setCoverageName(r.coverageName().trim());
  c.setMinimumCoverage(r.minimumCoverage());
  c.setMaximumCoverage(r.maximumCoverage());
  c.setWaitingPeriodDays(
          r.waitingPeriodDays() == null
                  ? 0 : r.waitingPeriodDays());
  c.setDeductibleAmount(
          r.deductibleAmount() == null
                  ? BigDecimal.ZERO : r.deductibleAmount());

  return coverageResponse(coverageRepo.save(c));
 }

 @Override
 @Transactional(readOnly = true)
 public List<CoverageResponse> getCoverages(Long id) {
  product(id);

  return coverageRepo.findAll()
          .stream()
          .filter(c -> c.getProduct().getProductId().equals(id))
          .map(this::coverageResponse)
          .toList();
 }

 @Override
 public CoverageResponse updateCoverage(Long id, CoverageRequest r) {
  validateCoverage(r);

  Coverage c = coverage(id);
  c.setCoverageName(r.coverageName().trim());
  c.setMinimumCoverage(r.minimumCoverage());
  c.setMaximumCoverage(r.maximumCoverage());
  c.setWaitingPeriodDays(
          r.waitingPeriodDays() == null
                  ? 0 : r.waitingPeriodDays());
  c.setDeductibleAmount(
          r.deductibleAmount() == null
                  ? BigDecimal.ZERO : r.deductibleAmount());

  return coverageResponse(coverageRepo.save(c));
 }

 @Override
 public void deleteCoverage(Long id) {
  coverageRepo.delete(coverage(id));
 }

 // ==================== RIDER APIs ====================

 @Override
 public RiderResponse addRider(Long id, RiderRequest r) {
  Rider x = new Rider();
  x.setProduct(product(id));
  x.setRiderName(r.riderName().trim());
  x.setRiderDescription(r.riderDescription());
  x.setRiderPremium(r.riderPremium());
  x.setStatus(normalizeRiderStatus(r.status()));

  return riderResponse(riderRepo.save(x));
 }

 @Override
 @Transactional(readOnly = true)
 public List<RiderResponse> getRiders(Long id) {
  product(id);

  return riderRepo.findByProduct_ProductId(id)
          .stream()
          .map(this::riderResponse)
          .toList();
 }

 @Override
 public RiderResponse updateRider(Long id, RiderRequest r) {
  Rider x = rider(id);
  x.setRiderName(r.riderName().trim());
  x.setRiderDescription(r.riderDescription());
  x.setRiderPremium(r.riderPremium());
  x.setStatus(normalizeRiderStatus(r.status()));

  return riderResponse(riderRepo.save(x));
 }

 @Override
 public void deleteRider(Long id) {
  Rider x = rider(id);
  x.setStatus(RiderStatus.INACTIVE);
  riderRepo.save(x);
 }

 // ==================== BENEFIT APIs ====================

 @Override
 public BenefitResponse addBenefit(Long id, BenefitRequest r) {
  Benefit b = new Benefit();
  b.setProduct(product(id));
  b.setBenefitName(r.benefitName().trim());
  b.setBenefitDescription(r.benefitDescription());
  b.setBenefitLimit(r.benefitLimit());

  return benefitResponse(benefitRepo.save(b));
 }

 @Override
 @Transactional(readOnly = true)
 public List<BenefitResponse> getBenefits(Long id) {
  product(id);

  return benefitRepo.findByProduct_ProductId(id)
          .stream()
          .map(this::benefitResponse)
          .toList();
 }

 @Override
 public BenefitResponse updateBenefit(Long id, BenefitRequest r) {
  Benefit b = benefit(id);
  b.setBenefitName(r.benefitName().trim());
  b.setBenefitDescription(r.benefitDescription());
  b.setBenefitLimit(r.benefitLimit());

  return benefitResponse(benefitRepo.save(b));
 }

 @Override
 public void deleteBenefit(Long id) {
  benefitRepo.delete(benefit(id));
 }

 // ==================== PREMIUM FREQUENCY APIs ====================

 @Override
 public PremiumFrequency createFrequency(FrequencyRequest r) {
  String name = r.frequencyName().trim();

  if (frequencyRepo.existsByFrequencyNameIgnoreCase(name)) {
   throw new BadRequestException(
           "Premium frequency already exists.");
  }

  PremiumFrequency f = new PremiumFrequency();
  f.setFrequencyName(name);
  f.setInstallmentCount(r.installmentCount());
  f.setStatus(
          r.status() == null || r.status().isBlank()
                  ? PremiumFrequencyStatus.ACTIVE
                  : PremiumFrequencyStatus.valueOf(
                  r.status().trim().toUpperCase(java.util.Locale.ROOT)
          )
  );
  return frequencyRepo.save(f);
 }

 @Override
 @Transactional(readOnly = true)
 public List<PremiumFrequency> getFrequencies() {
  return frequencyRepo.findAll();
 }

 @Override
 public PremiumFrequency updateFrequency(Long id, FrequencyRequest r) {
  PremiumFrequency f = frequencyRepo.findById(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Premium frequency not found: " + id));

  f.setFrequencyName(r.frequencyName().trim());
  f.setInstallmentCount(r.installmentCount());
  f.setStatus(
          r.status() == null
                  ? PremiumFrequencyStatus.ACTIVE
                  : PremiumFrequencyStatus.valueOf(r.status().trim().toUpperCase())
  );
  return frequencyRepo.save(f);
 }

 // ==================== COMMISSION APIs ====================

 @Override
 public CommissionResponse saveCommission(Long id, CommissionRequest r) {
  InsuranceProduct p = product(id);

  ProductCommission c = commissionRepo
          .findByProduct_ProductId(id)
          .orElseGet(ProductCommission::new);

  c.setProduct(p);
  c.setFirstYearCommission(r.firstYearCommission());
  c.setRenewalCommission(r.renewalCommission());
  c.setBranchCommission(r.branchCommission());

  return commissionResponse(commissionRepo.save(c));
 }

 @Override
 @Transactional(readOnly = true)
 public CommissionResponse getCommission(Long id) {
  product(id);

  ProductCommission c = commissionRepo
          .findByProduct_ProductId(id)
          .orElseThrow(() ->
                  new ResourceNotFoundException(
                          "Commission configuration not found for product: " + id));

  return commissionResponse(c);
 }
}
