//
//package org.example.insurance_policy_system.serviceimpl.policyclosure;
//
//import lombok.RequiredArgsConstructor;
//import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureRequestDto;
//import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureResponseDto;
//import org.example.insurance_policy_system.entity.policy_insurance.Policy;
//import org.example.insurance_policy_system.entity.policy_insurance.PolicyHistory;
//import org.example.insurance_policy_system.entity.policy_insurance.PolicyStatus;
//import org.example.insurance_policy_system.entity.policyclosure.PolicyClosure;
//import org.example.insurance_policy_system.entity.policyclosure.PolicyClosureStatus;
//import org.example.insurance_policy_system.entity.policyclosure.PolicyClosureType;
//import org.example.insurance_policy_system.exception.BadRequestException;
//import org.example.insurance_policy_system.exception.ResourceNotFoundException;
//import org.example.insurance_policy_system.repository.policy_insurance.PolicyHistoryRepository;
//import org.example.insurance_policy_system.repository.policy_insurance.PolicyRepository;
//import org.example.insurance_policy_system.repository.policyclosure.PolicyClosureRepository;
//import org.example.insurance_policy_system.service.audit.AuditService;
//import org.example.insurance_policy_system.service.policyclosure.PolicyClosureService;
//import org.modelmapper.ModelMapper;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PolicyClosureServiceImpl implements PolicyClosureService {
//
//    private final PolicyRepository policyRepository;
//    private final PolicyHistoryRepository policyHistoryRepository;
//    private final PolicyClosureRepository closureRepository;
//    private final AuditService auditService;
//    private final ModelMapper modelMapper;
//
//    @Override
//    public PolicyClosureResponseDto expirePolicy(Long policyId, PolicyClosureRequestDto request) {
//
//        if (policyId == null || policyId <= 0) {
//            throw new BadRequestException("Policy ID must be positive");
//        }
//
//        Policy policy = policyRepository.findById(policyId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Policy not found with ID: " + policyId));
//
//        validateMaturityClosure(policy);
//
//        return closeMaturedPolicy(policy, request);
//    }
//
//    private void validateMaturityClosure(Policy policy) {
//
//        if (policy.getPolicyStatus() == PolicyStatus.CANCELLED) {
//            throw new BadRequestException(
//                    "Cancelled policy cannot be maturity-closed");
//        }
//
//        if (policy.getPolicyStatus() == PolicyStatus.EXPIRED || policy.getPolicyStatus() == PolicyStatus.CLOSED) {
//            throw new BadRequestException(
//                    "Policy is already expired or closed");
//        }
//
//        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
//            throw new BadRequestException(
//                    "Only an ACTIVE policy can be maturity-closed");
//        }
//
//        if (policy.getExpiryDate() == null) {
//            throw new BadRequestException(
//                    "Policy expiry date is missing");
//        }
//
//        if (policy.getExpiryDate().isAfter(LocalDate.now())) {
//            throw new BadRequestException(
//                    "Policy term has not ended yet. Expiry date: "
//                            + policy.getExpiryDate());
//        }
//
//        if (closureRepository.existsByPolicyIdAndClosureType(policy.getPolicyId(), PolicyClosureType.MATURITY)) {
//            throw new BadRequestException(
//                    "Maturity closure already exists for this policy");
//        }
//    }
//
//    private PolicyClosureResponseDto closeMaturedPolicy(Policy policy, PolicyClosureRequestDto request) {
//
//        String oldStatus = policy.getPolicyStatus().name();
//
//        policy.setPolicyStatus(PolicyStatus.EXPIRED);
//        policyRepository.save(policy);
//
//        String closureNumber = "PC-"
//                + LocalDateTime.now().format(
//                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
//                + "-"
//                + UUID.randomUUID()
//                .toString()
//                .substring(0, 8)
//                .toUpperCase();
//
//        Long closedBy = request == null ? null : request.getClosedBy();
//        String remarks = request == null ? null : request.getRemarks();
//
//        PolicyClosure closure = PolicyClosure.builder()
//                .policyId(policy.getPolicyId())
//                .closureNumber(closureNumber)
//                .closureType(PolicyClosureType.MATURITY)
//                .closureStatus(PolicyClosureStatus.COMPLETED)
//                .policyExpiryDate(policy.getExpiryDate())
//                .closedBy(closedBy)
//                .remarks(remarks)
//                .build();
//
//        PolicyClosure savedClosure = closureRepository.save(closure);
//
//        PolicyHistory history = PolicyHistory.builder()
//                .policyId(policy.getPolicyId())
//                .action("POLICY_EXPIRED_MATURITY_CLOSURE")
//                .performedBy(closedBy)
//                .actionDate(LocalDateTime.now())
//                .remarks(remarks == null || remarks.isBlank()
//                        ? "Policy term ended; status changed to EXPIRED."
//                        : remarks)
//                .build();
//
//        policyHistoryRepository.save(history);
//
//        auditService.logAction(
//                closedBy,
//                "POLICY_CLOSURE",
//                "POLICY_MATURITY_CLOSED",
//                oldStatus,
//                PolicyStatus.EXPIRED.name(),
//                null,
//                "Maturity closure completed. Closure number: "
//                        + savedClosure.getClosureNumber());
//
//        return modelMapper.map(
//                savedClosure, PolicyClosureResponseDto.class);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PolicyClosureResponseDto getClosureById(Long closureId) {
//
//        PolicyClosure closure = closureRepository.findById(closureId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Policy closure not found with ID: " + closureId));
//
//        return modelMapper.map(
//                closure, PolicyClosureResponseDto.class);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<PolicyClosureResponseDto> getClosuresByPolicyId(Long policyId) {
//
//        if (!policyRepository.existsById(policyId)) {
//            throw new ResourceNotFoundException(
//                    "Policy not found with ID: " + policyId);
//        }
//
//        return closureRepository
//                .findByPolicyIdOrderByClosureDateDesc(policyId)
//                .stream()
//                .map(closure -> modelMapper.map(
//                        closure, PolicyClosureResponseDto.class))
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<PolicyClosureResponseDto> getAllClosures() {
//
//        return closureRepository.findAll()
//                .stream()
//                .map(closure -> modelMapper.map(
//                        closure, PolicyClosureResponseDto.class))
//                .toList();
//    }
//
//    @Override
//    @Scheduled(cron = "0 0 1 * * *")
//    public void processMaturedPolicies() {
//
//        List<Policy> maturedPolicies = policyRepository.findByPolicyStatusAndExpiryDateLessThanEqual(
//                        PolicyStatus.ACTIVE,
//                        LocalDate.now());
//
//        for (Policy policy : maturedPolicies) {
//
//            boolean closureExists =
//                    closureRepository.existsByPolicyIdAndClosureType(
//                            policy.getPolicyId(),
//                            PolicyClosureType.MATURITY);
//
//            if (closureExists) {
//                policy.setPolicyStatus(PolicyStatus.EXPIRED);
//                policyRepository.save(policy);
//                continue;
//            }
//
//            closeMaturedPolicy(policy, null);
//        }
//    }
//}
