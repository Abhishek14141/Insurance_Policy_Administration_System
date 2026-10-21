package org.example.insurance_policy_system.service.policyservicing;


import org.example.insurance_policy_system.dto.policyservicing.PolicyRenewalRequestDTO;
import org.example.insurance_policy_system.dto.policyservicing.PolicyRenewalResponseDTO;

public interface PolicyRenewalService {

    PolicyRenewalResponseDTO renewPolicy(
            Long policyId,
            PolicyRenewalRequestDTO requestDTO);

    PolicyRenewalResponseDTO getRenewalById(
            Long renewalId);
}

