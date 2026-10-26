package org.example.insurance_policy_system.serviceimpl.policyclosure;

import lombok.RequiredArgsConstructor;

import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureResponseDto;
import org.example.insurance_policy_system.dto.policyclosure.PolicyExpiryCheckResponseDto;
import org.example.insurance_policy_system.dto.policyclosure.PolicySummaryDto;
import org.example.insurance_policy_system.entity.policyclosure.ClosureStatus;
import org.example.insurance_policy_system.entity.policyclosure.ClosureType;
import org.example.insurance_policy_system.entity.policyclosure.PolicyClosure;
import org.example.insurance_policy_system.repository.policyclosure.PolicyClosureRepository;
import org.example.insurance_policy_system.service.policyclosure.PolicyClosureService;
import org.example.policyclosureservice.dto.PolicyClosureRequestDto;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PolicyClosureServiceImpl implements PolicyClosureService {

    private final PolicyClosureRepository closureRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public PolicyExpiryCheckResponseDto checkExpiry(Long policyId) {
        PolicySummaryDto policy = policyClient.getPolicy(policyId);
        String reason = closureBlockReason(policy);

        return PolicyExpiryCheckResponseDto.builder()
                .policyId(policy.getPolicyId())
                .policyNo(policy.getPolicyNo())
                .policyStatus(policy.getPolicyStatus())
                .expiryDate(policy.getExpiryDate())
                .checkedDate(LocalDate.now())
                .eligibleForClosure(reason == null)
                .reason(reason == null ? "Policy is eligible for manual expiry" : reason)
                .build();
    }

    @Override
    @Transactional
    public PolicyClosureResponseDto expirePolicy(Long policyId, PolicyClosureRequestDto request) {
        PolicySummaryDto policy = policyClient.getPolicy(policyId);
        String reason = closureBlockReason(policy);
        if (reason != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, reason);
        }

        policyClient.markExpired(policyId);

        String closureNumber = "PC-"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        PolicyClosure closure = PolicyClosure.builder()
                .policyId(policyId)
                .closureNumber(closureNumber)
                .closureType(ClosureType.MATURITY)
                .closureStatus(ClosureStatus.COMPLETED)
                .policyExpiryDate(policy.getExpiryDate())
                .closedBy(request.getClosedBy())
                .remarks(request.getRemarks())
                .build();

        return modelMapper.map(closureRepository.save(closure), PolicyClosureResponseDto.class);
    }

    private String closureBlockReason(PolicySummaryDto policy) {
        if (policy == null || policy.getPolicyId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Policy Service returned invalid policy data");
        }
        if (closureRepository.existsByPolicyIdAndClosureType(
                policy.getPolicyId(), ClosureType.MATURITY)) {
            return "This policy already has a maturity closure record";
        }
        String status = policy.getPolicyStatus();
        if (status == null || !"ACTIVE".equalsIgnoreCase(status.trim())) {
            return "Only ACTIVE policies can be manually expired";
        }
        if (policy.getExpiryDate() == null) {
            return "Policy expiry date is missing";
        }
        if (policy.getExpiryDate().isAfter(LocalDate.now())) {
            return "Policy term has not ended yet";
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyClosureResponseDto getClosureById(Long closureId) {
        PolicyClosure closure = closureRepository.findById(closureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Policy closure not found with ID: " + closureId));
        return modelMapper.map(closure, PolicyClosureResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyClosureResponseDto> getClosuresByPolicyId(Long policyId) {
        return closureRepository.findByPolicyIdOrderByClosureDateDesc(policyId)
                .stream().map(closure -> modelMapper.map(closure, PolicyClosureResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyClosureResponseDto> getAllClosures() {
        return closureRepository.findAll().stream()
                .map(closure -> modelMapper.map(closure, PolicyClosureResponseDto.class))
                .toList();
    }
}
