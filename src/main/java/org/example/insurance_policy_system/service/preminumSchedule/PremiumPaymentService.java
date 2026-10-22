package org.example.insurance_policy_system.service.preminumSchedule;

import com.ampta.premiumservice.dto.PaymentRequestDto;
import com.ampta.premiumservice.dto.PaymentResponseDto;

public interface PremiumPaymentService {
    PaymentResponseDto payPremium(PaymentRequestDto request);
}
