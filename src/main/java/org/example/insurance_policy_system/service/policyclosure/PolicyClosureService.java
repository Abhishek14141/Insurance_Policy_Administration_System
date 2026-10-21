
package org.example.insurance_policy_system.service.policyclosure;

import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureRequestDto;
import org.example.insurance_policy_system.dto.policyclosure.PolicyClosureResponseDto;

import java.util.List;

public interface PolicyClosureService {

    PolicyClosureResponseDto expirePolicy(Long policyId, PolicyClosureRequestDto request);
    PolicyClosureResponseDto getClosureById(Long closureId);
    List<PolicyClosureResponseDto> getClosuresByPolicyId(Long policyId);
    List<PolicyClosureResponseDto> getAllClosures();
    void processMaturedPolicies();
}
