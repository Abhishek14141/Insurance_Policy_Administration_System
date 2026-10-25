package org.example.insurance_policy_system.service.preminumSchedule.impl;

import com.ampta.premiumservice.client.PolicyClient;
import com.ampta.premiumservice.dto.PaymentRequestDto;
import com.ampta.premiumservice.dto.PaymentResponseDto;
import com.ampta.premiumservice.repository.PremiumPaymentRepository;
import com.ampta.premiumservice.repository.PremiumScheduleRepository;
import com.ampta.premiumservice.service.PremiumPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PremiumPaymentServiceImpl implements PremiumPaymentService {

    private final PremiumPaymentRepository paymentRepository;
    private final PremiumScheduleRepository scheduleRepository;
    private final PolicyClient policyClient;

    @Override
    public PaymentResponseDto payPremium(PaymentRequestDto request) {
        return null;
    }
}
