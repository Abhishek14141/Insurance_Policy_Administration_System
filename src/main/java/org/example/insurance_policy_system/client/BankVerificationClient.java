package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationRequestDto;
import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationResponseDto;

public interface BankVerificationClient {

    BankVerificationResponseDto verify(Long claimId, BankVerificationRequestDto request);
}