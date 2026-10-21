package org.example.insurance_policy_system.serviceimpl.claimsettlement;

import org.example.insurance_policy_system.dto.claimsettlement.BankVerificationRequestDto;
import org.example.insurance_policy_system.service.claimsettlement.BankVerificationService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class BankVerificationServiceImpl
        implements BankVerificationService {

    @Override
    public boolean verifyBankAccount(
            BankVerificationRequestDto request) {

        if (request == null) {
            return false;
        }

        String accountNumber = request.getAccountNumber();
        String ifscCode = request.getIfscCode();
        String holderName = request.getAccountHolderName();

        if (accountNumber == null ||
                !accountNumber.matches("\\d{9,18}")) {
            return false;
        }

        if (ifscCode == null ||
                !ifscCode.toUpperCase(Locale.ROOT)
                        .matches("^[A-Z]{4}0[A-Z0-9]{6}$")) {
            return false;
        }

        if (holderName == null ||
                holderName.isBlank()) {
            return false;
        }

        return true;
    }
}