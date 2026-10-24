package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;

import java.util.List;

public interface ProductMasterService {
    CoverageResponseDto addCoverage(Long productId, CoverageRequestDto dto);

    List<CoverageResponseDto> getCoverages(Long productId);

    CoverageResponseDto updateCoverage(Long id, CoverageRequestDto dto);

    void deleteCoverage(Long id);

    RiderResponseDto addRider(Long productId, RiderRequestDto dto);

    List<RiderResponseDto> getRiders(Long productId);

    RiderResponseDto updateRider(Long id, RiderRequestDto dto);

    void deleteRider(Long id);

    BenefitResponseDto addBenefit(Long productId, BenefitRequestDto dto);

    List<BenefitResponseDto> getBenefits(Long productId);

    BenefitResponseDto updateBenefit(Long id, BenefitRequestDto dto);

    void deleteBenefit(Long id);
}
