package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;

import java.util.List;

public interface ProductConfigurationService {
    PremiumFrequencyResponseDto createFrequency(PremiumFrequencyRequestDto request);

    List<PremiumFrequencyResponseDto> getFrequencies();

    ProductCommissionResponseDto saveCommission(Long productId, ProductCommissionRequestDto request);

    ProductCommissionResponseDto getCommission(Long productId);
}
