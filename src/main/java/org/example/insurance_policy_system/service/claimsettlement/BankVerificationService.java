package org.example.insurance_policy_system.service.claimsettlement;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.client.BankVerificationClient;
import org.example.insurance_policy_system.client.ClaimClient;
import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationRequestDto;
import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationResponseDto;
import org.example.insurance_policy_system.dto.claimsettlement.ClaimResponseDto;
import org.springframework.stereotype.Service;


public interface BankVerificationService {

    public boolean verifyBankAccount(BankVerificationRequestDto request) ;


}