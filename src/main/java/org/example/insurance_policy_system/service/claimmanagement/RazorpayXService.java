package org.example.insurance_policy_system.service.claimmanagement;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;

public interface RazorpayXService {

    String createContact(String name, Long claimId);

    String createFundAccount(
            String contactId,
            String name,
            String accountNumber,
            String ifsc
    );

    JsonNode createPayout(
            String fundAccountId,
            BigDecimal amount,
            String mode,
            String idempotencyKey
    );

    JsonNode getPayout(String payoutId);
}