package org.example.insurance_policy_system.service.policyclosure;

import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureResponseDto;
import org.example.insurance_policy_system.dto.policyclosure.PolicyExpiryCheckResponseDto;
import org.example.policyclosureservice.dto.PolicyClosureRequestDto;
import java.util.List;

public interface PolicyClosureService {
    PolicyExpiryCheckResponseDto checkExpiry(Long policyId);
    PolicyClosureResponseDto expirePolicy(Long policyId, PolicyClosureRequestDto request);
    PolicyClosureResponseDto getClosureById(Long closureId);
    List<PolicyClosureResponseDto> getClosuresByPolicyId(Long policyId);
    List<PolicyClosureResponseDto> getAllClosures();
}
