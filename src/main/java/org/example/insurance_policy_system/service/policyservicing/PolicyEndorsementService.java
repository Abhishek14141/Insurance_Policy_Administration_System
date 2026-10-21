package org.example.insurance_policy_system.service.policyservicing;


import org.example.insurance_policy_system.dto.policyservicing.PolicyEndorsementRequestDTO;
import org.example.insurance_policy_system.dto.policyservicing.PolicyEndorsementResponseDTO;

public interface PolicyEndorsementService {

    PolicyEndorsementResponseDTO createEndorsement(
            PolicyEndorsementRequestDTO requestDTO);

    PolicyEndorsementResponseDTO getEndorsementById(
            Long endorsementId);

    PolicyEndorsementResponseDTO approveEndorsement(
            Long endorsementId);

    PolicyEndorsementResponseDTO rejectEndorsement(
            Long endorsementId);
}

