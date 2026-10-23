package org.example.insurance_policy_system.serviceimpl.insuranceManagement;

import org.example.insurance_policy_system.dto.insuranceManagement.*;
import org.example.insurance_policy_system.entity.insuranceManagement.*;
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

 public ProductManagementServiceImpl(ProductCategoryRepository categoryRepo, InsuranceProductRepository productRepo,
                                     CoverageRepository coverageRepo, RiderRepository riderRepo, BenefitRepository benefitRepo,
                                     PremiumFrequencyRepository frequencyRepo, ProductCommissionRepository commissionRepo) {
  this.categoryRepo=categoryRepo; this.productRepo=productRepo; this.coverageRepo=coverageRepo;
  this.riderRepo=riderRepo; this.benefitRepo=benefitRepo; this.frequencyRepo=frequencyRepo; this.commissionRepo=commissionRepo;
 }

 private ProductCategory category(Long id) { return categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found: "+id)); }
 private InsuranceProduct product(Long id) { return productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found: "+id)); }
 private Coverage coverage(Long id) { return coverageRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Coverage not found: "+id)); }
 private Rider rider(Long id) { return riderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Rider not found: "+id)); }
 private Benefit benefit(Long id) { return benefitRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Benefit not found: "+id)); }
 private void validateRange(Integer minAge,Integer maxAge,BigDecimal minSum,BigDecimal maxSum) {
  if(minAge!=null && maxAge!=null && minAge>maxAge) throw new BadRequestException("Minimum age cannot exceed maximum age.");
  if(minSum!=null && maxSum!=null && minSum.compareTo(maxSum)>0) throw new BadRequestException("Minimum sum insured cannot exceed maximum sum insured.");
 }
 private CategoryResponse catResponse(ProductCategory c) { return new CategoryResponse(c.getCategoryId(),c.getCategoryName(),c.getDescription(),c.getStatus()); }
 private ProductResponse productResponse(InsuranceProduct p) {
  return new ProductResponse(p.getProductId(),p.getProductCode(),p.getProductName(),p.getProductType(),p.getDescription(),
   p.getCategory().getCategoryId(),p.getCategory().getCategoryName(),p.getMinimumAge(),p.getMaximumAge(),
   p.getMinimumSumInsured(),p.getMaximumSumInsured(),p.getStatus());
 }
 private CoverageResponse coverageResponse(Coverage c) { return new CoverageResponse(c.getCoverageId(),c.getProduct().getProductId(),c.getCoverageName(),c.getMinimumCoverage(),c.getMaximumCoverage(),c.getWaitingPeriodDays(),c.getDeductibleAmount()); }
 private RiderResponse riderResponse(Rider r) { return new RiderResponse(r.getRiderId(),r.getProduct().getProductId(),r.getRiderName(),r.getRiderDescription(),r.getRiderPremium(),r.getStatus()); }
 private BenefitResponse benefitResponse(Benefit b) { return new BenefitResponse(b.getBenefitId(),b.getProduct().getProductId(),b.getBenefitName(),b.getBenefitDescription(),b.getBenefitLimit()); }
 private CommissionResponse commissionResponse(ProductCommission c) { return new CommissionResponse(c.getCommissionId(),c.getProduct().getProductId(),c.getFirstYearCommission(),c.getRenewalCommission(),c.getBranchCommission()); }

 @Override public CategoryResponse createCategory(CategoryRequest r) {
  if(categoryRepo.existsByCategoryNameIgnoreCase(r.categoryName().trim())) throw new BadRequestException("Category name already exists.");
  ProductCategory c=new ProductCategory(); c.setCategoryName(r.categoryName().trim()); c.setDescription(r.description()); c.setStatus("ACTIVE");
  return catResponse(categoryRepo.save(c));
 }
 @Override @Transactional(readOnly=true) public List<CategoryResponse> getCategories() { return categoryRepo.findAll().stream().map(this::catResponse).toList(); }
 @Override public CategoryResponse updateCategory(Long id,CategoryRequest r) {
  ProductCategory c=category(id);
  categoryRepo.findAll().stream().filter(x->x.getCategoryName().equalsIgnoreCase(r.categoryName().trim())&&!x.getCategoryId().equals(id)).findAny()
   .ifPresent(x->{throw new BadRequestException("Category name already exists.");});
  c.setCategoryName(r.categoryName().trim()); c.setDescription(r.description()); return catResponse(categoryRepo.save(c));
 }
 @Override public CategoryResponse deactivateCategory(Long id) {
  ProductCategory c=category(id);
  if(productRepo.countByCategory_CategoryId(id)>0) throw new BadRequestException("Category has products; deactivate the products or move them first.");
  c.setStatus("INACTIVE"); return catResponse(categoryRepo.save(c));
 }

 @Override public ProductResponse createProduct(ProductRequest r) {
  validateRange(r.minimumAge(),r.maximumAge(),r.minimumSumInsured(),r.maximumSumInsured());
  if(productRepo.existsByProductNameIgnoreCase(r.productName().trim())) throw new BadRequestException("Product name already exists.");
  ProductCategory c=category(r.categoryId());
  if(!"ACTIVE".equalsIgnoreCase(c.getStatus())) throw new BadRequestException("Cannot create product under an inactive category.");
  InsuranceProduct p=new InsuranceProduct(); p.setCategory(c); p.setProductName(r.productName().trim()); p.setProductType(r.productType().trim());
  p.setDescription(r.description()); p.setMinimumAge(r.minimumAge()); p.setMaximumAge(r.maximumAge());
  p.setMinimumSumInsured(r.minimumSumInsured()); p.setMaximumSumInsured(r.maximumSumInsured());
  p.setProductCode("IP-"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase(Locale.ROOT));
  p.setStatus("INACTIVE");
  return productResponse(productRepo.save(p));
 }
 @Override @Transactional(readOnly=true) public List<ProductResponse> getProducts() { return productRepo.findAll().stream().map(this::productResponse).toList(); }
 @Override @Transactional(readOnly=true) public List<ProductResponse> getActiveProducts() { return productRepo.findByStatusIgnoreCase("ACTIVE").stream().map(this::productResponse).toList(); }
 @Override @Transactional(readOnly=true) public ProductResponse getProduct(Long id) { return productResponse(product(id)); }
 @Override public ProductResponse updateProduct(Long id,ProductRequest r) {
  validateRange(r.minimumAge(),r.maximumAge(),r.minimumSumInsured(),r.maximumSumInsured());
  InsuranceProduct p=product(id);
  if(productRepo.existsByProductNameIgnoreCaseAndProductIdNot(r.productName().trim(),id)) throw new BadRequestException("Product name already exists.");
  ProductCategory c=category(r.categoryId());
  if(!"ACTIVE".equalsIgnoreCase(c.getStatus())) throw new BadRequestException("Cannot assign an inactive category.");
  p.setCategory(c); p.setProductName(r.productName().trim()); p.setProductType(r.productType().trim()); p.setDescription(r.description());
  p.setMinimumAge(r.minimumAge()); p.setMaximumAge(r.maximumAge()); p.setMinimumSumInsured(r.minimumSumInsured()); p.setMaximumSumInsured(r.maximumSumInsured());
  return productResponse(productRepo.save(p));
 }
 @Override public ProductResponse changeProductStatus(Long id,String status) {
  InsuranceProduct p=product(id); String normalized=status==null?"":status.trim().toUpperCase(Locale.ROOT);
  if(!Set.of("ACTIVE","INACTIVE").contains(normalized)) throw new BadRequestException("Status must be ACTIVE or INACTIVE.");
  if("ACTIVE".equals(normalized) && (p.getMinimumSumInsured()==null || p.getMaximumSumInsured()==null))
   throw new BadRequestException("Configure minimum and maximum sum insured before activating a product.");
  p.setStatus(normalized); return productResponse(productRepo.save(p));
 }
 @Override @Transactional(readOnly=true) public DashboardResponse getDashboard() {
  List<InsuranceProduct> all=productRepo.findAll();
  Map<String,Long> byCategory=all.stream().collect(Collectors.groupingBy(p->p.getCategory().getCategoryName(),TreeMap::new,Collectors.counting()));
  long withRiders=all.stream().filter(p->!riderRepo.findByProduct_ProductId(p.getProductId()).isEmpty()).count();
  return new DashboardResponse(all.size(),productRepo.countByStatusIgnoreCase("ACTIVE"),productRepo.countByStatusIgnoreCase("INACTIVE"),withRiders,byCategory);
 }

 @Override public CoverageResponse addCoverage(Long productId,CoverageRequest r) {
  InsuranceProduct p=product(productId); validateCoverage(r);
  Coverage c=new Coverage(); c.setProduct(p); c.setCoverageName(r.coverageName().trim()); c.setMinimumCoverage(r.minimumCoverage());
  c.setMaximumCoverage(r.maximumCoverage()); c.setWaitingPeriodDays(r.waitingPeriodDays()==null?0:r.waitingPeriodDays());
  c.setDeductibleAmount(r.deductibleAmount()==null?BigDecimal.ZERO:r.deductibleAmount()); return coverageResponse(coverageRepo.save(c));
 }
 private void validateCoverage(CoverageRequest r) { if(r.minimumCoverage().compareTo(r.maximumCoverage())>0) throw new BadRequestException("Minimum coverage cannot exceed maximum coverage."); }
 @Override @Transactional(readOnly=true) public List<CoverageResponse> getCoverages(Long id) { product(id); return coverageRepo.findAll().stream().filter(c->c.getProduct().getProductId().equals(id)).map(this::coverageResponse).toList(); }
 @Override public CoverageResponse updateCoverage(Long id,CoverageRequest r) {
  validateCoverage(r); Coverage c=coverage(id); c.setCoverageName(r.coverageName().trim()); c.setMinimumCoverage(r.minimumCoverage()); c.setMaximumCoverage(r.maximumCoverage());
  c.setWaitingPeriodDays(r.waitingPeriodDays()==null?0:r.waitingPeriodDays()); c.setDeductibleAmount(r.deductibleAmount()==null?BigDecimal.ZERO:r.deductibleAmount()); return coverageResponse(coverageRepo.save(c));
 }
 @Override public void deleteCoverage(Long id) { coverageRepo.delete(coverage(id)); }

 @Override public RiderResponse addRider(Long id,RiderRequest r) {
  Rider x=new Rider(); x.setProduct(product(id)); x.setRiderName(r.riderName().trim()); x.setRiderDescription(r.riderDescription());
  x.setRiderPremium(r.riderPremium()); x.setStatus(normalizeStatus(r.status(),"ACTIVE")); return riderResponse(riderRepo.save(x));
 }
 @Override @Transactional(readOnly=true) public List<RiderResponse> getRiders(Long id) { product(id); return riderRepo.findByProduct_ProductId(id).stream().map(this::riderResponse).toList(); }
 @Override public RiderResponse updateRider(Long id,RiderRequest r) { Rider x=rider(id); x.setRiderName(r.riderName().trim()); x.setRiderDescription(r.riderDescription()); x.setRiderPremium(r.riderPremium()); x.setStatus(normalizeStatus(r.status(),"ACTIVE")); return riderResponse(riderRepo.save(x)); }
 @Override public void deleteRider(Long id) { Rider x=rider(id); x.setStatus("INACTIVE"); riderRepo.save(x); }
 @Override public BenefitResponse addBenefit(Long id,BenefitRequest r) { Benefit b=new Benefit(); b.setProduct(product(id)); b.setBenefitName(r.benefitName().trim()); b.setBenefitDescription(r.benefitDescription()); b.setBenefitLimit(r.benefitLimit()); return benefitResponse(benefitRepo.save(b)); }
 @Override @Transactional(readOnly=true) public List<BenefitResponse> getBenefits(Long id) { product(id); return benefitRepo.findByProduct_ProductId(id).stream().map(this::benefitResponse).toList(); }
 @Override public BenefitResponse updateBenefit(Long id,BenefitRequest r) { Benefit b=benefit(id); b.setBenefitName(r.benefitName().trim()); b.setBenefitDescription(r.benefitDescription()); b.setBenefitLimit(r.benefitLimit()); return benefitResponse(benefitRepo.save(b)); }
 @Override public void deleteBenefit(Long id) { benefitRepo.delete(benefit(id)); }

 @Override public PremiumFrequency createFrequency(FrequencyRequest r) {
  if(frequencyRepo.existsByFrequencyNameIgnoreCase(r.frequencyName().trim())) throw new BadRequestException("Premium frequency already exists.");
  PremiumFrequency f=new PremiumFrequency(); f.setFrequencyName(r.frequencyName().trim()); f.setInstallmentCount(r.installmentCount()); f.setStatus(normalizeStatus(r.status(),"ACTIVE")); return frequencyRepo.save(f);
 }
 @Override @Transactional(readOnly=true) public List<PremiumFrequency> getFrequencies() { return frequencyRepo.findAll(); }
 @Override public PremiumFrequency updateFrequency(Long id,FrequencyRequest r) {
  PremiumFrequency f=frequencyRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Premium frequency not found: "+id));
  f.setFrequencyName(r.frequencyName().trim()); f.setInstallmentCount(r.installmentCount()); f.setStatus(normalizeStatus(r.status(),"ACTIVE")); return frequencyRepo.save(f);
 }
 @Override public CommissionResponse saveCommission(Long id,CommissionRequest r) {
  InsuranceProduct p=product(id); ProductCommission c=commissionRepo.findByProduct_ProductId(id).orElseGet(ProductCommission::new);
  c.setProduct(p); c.setFirstYearCommission(r.firstYearCommission()); c.setRenewalCommission(r.renewalCommission()); c.setBranchCommission(r.branchCommission()); return commissionResponse(commissionRepo.save(c));
 }
 @Override @Transactional(readOnly=true) public CommissionResponse getCommission(Long id) {
  product(id); ProductCommission c=commissionRepo.findByProduct_ProductId(id).orElseThrow(()->new ResourceNotFoundException("Commission configuration not found for product: "+id)); return commissionResponse(c);
 }
 private String normalizeStatus(String status,String fallback) {
  String s=(status==null||status.isBlank()?fallback:status).trim().toUpperCase(Locale.ROOT);
  if(!Set.of("ACTIVE","INACTIVE").contains(s)) throw new BadRequestException("Status must be ACTIVE or INACTIVE."); return s;
 }
}
