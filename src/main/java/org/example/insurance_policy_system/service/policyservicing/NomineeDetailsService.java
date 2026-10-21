package org.example.insurance_policy_system.service.policyservicing;



import org.example.insurance_policy_system.dto.policyservicing.NomineeDetailsRequestDTO;
import org.example.insurance_policy_system.dto.policyservicing.NomineeDetailsResponseDTO;

import java.util.List;

public interface NomineeDetailsService {

    NomineeDetailsResponseDTO addNominee(
            NomineeDetailsRequestDTO requestDTO);

    NomineeDetailsResponseDTO updateNominee(
            Long nomineeId,
            NomineeDetailsRequestDTO requestDTO);

    NomineeDetailsResponseDTO getNomineeById(
            Long nomineeId);

    List<NomineeDetailsResponseDTO> getNomineesByPolicyId(
            Long policyId);

    void deleteNominee(Long nomineeId);
}

