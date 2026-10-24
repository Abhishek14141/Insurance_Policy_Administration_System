package org.example.insurance_policy_system.service.claimmanagement;

import org.example.insurance_policy_system.dto.claimmanagement.*;

public interface BankVerificationService {

    BankDetailsResponseDto save(
            Long claimId,
            BankDetailsRequestDto request
    );

    BankDetailsResponseDto demoVerify(Long claimId);
}