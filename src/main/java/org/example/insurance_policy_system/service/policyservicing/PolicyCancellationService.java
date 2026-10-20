package org.example.insurance_policy_system.service.policyservicing;


import org.example.insurance_policy_system.dto.policyservicing.PolicyCancellationRequestDTO;
import org.example.insurance_policy_system.dto.policyservicing.PolicyCancellationResponseDTO;

public interface PolicyCancellationService {

    PolicyCancellationResponseDTO cancelPolicy(
            Long policyId,
            PolicyCancellationRequestDTO requestDTO);

    PolicyCancellationResponseDTO getCancellationById(
            Long cancellationId);
}

