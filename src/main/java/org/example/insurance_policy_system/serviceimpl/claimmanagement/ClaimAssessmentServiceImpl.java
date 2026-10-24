//
//package org.example.insurance_policy_system.serviceimpl.claimmanagement;
//
//import lombok.RequiredArgsConstructor;
//import org.example.insurance_policy_system.dto.claimmanagement.*;
//import org.example.insurance_policy_system.entity.claimmanagement.*;
//import org.example.insurance_policy_system.enums.claimmanagement.DocumentVerificationStatus;
//import org.example.insurance_policy_system.repository.claimmanagement.*;
//import org.example.insurance_policy_system.service.claimmanagement.*;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ClaimAssessmentServiceImpl
//        implements ClaimAssessmentService {
//
//    private static final Long TEST_EMPLOYEE_ID = 1L;
//
//    private final ClaimMasterRepository claimRepository;
//    private final ClaimAssessmentRepository assessmentRepository;
//    private final ClaimDocumentsRepository documentRepository;
//    private final ClaimHistoryService historyService;
//
//    // 1. CREATE ASSESSMENT
//
//    @Override
//    @Transactional
//    public ClaimAssessmentResponseDto createAssessment(
//            Long claimId,
//            ClaimAssessmentRequestDto requestDto) {
//
//        ClaimMaster claim = findClaim(claimId);
//
//        if (assessmentRepository.existsByClaim_ClaimId(claimId)) {
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Assessment already exists"
//            );
//        }
//
//        validateDocuments(claimId);
//        validateAmounts(requestDto);
//
//        BigDecimal netPayable = calculateNetPayable(requestDto);
//
//        ClaimAssessment assessment = new ClaimAssessment();
//
//        assessment.setClaim(claim);
//        assessment.setAssessedAmount(requestDto.getAssessedAmount());
//        assessment.setDeductible(requestDto.getDeductible());
//        assessment.setDepreciation(requestDto.getDepreciation());
//        assessment.setNonPayable(requestDto.getNonPayable());
//        assessment.setNetPayable(netPayable);
//
//        ClaimAssessment saved =
//                assessmentRepository.saveAndFlush(assessment);
//
//        historyService.recordHistory(
//                claimId,
//                "CLAIM_ASSESSED",
//                TEST_EMPLOYEE_ID,
//                "Net payable: " + netPayable
//        );
//
//        return toResponse(saved);
//    }
//
//    // 2. GET ASSESSMENT
//
//    @Override
//    @Transactional(readOnly = true)
//    public ClaimAssessmentResponseDto getAssessment(Long claimId) {
//
//        ClaimAssessment assessment = assessmentRepository
//                .findByClaim_ClaimId(claimId)
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        "Assessment not found"
//                ));
//
//        return toResponse(assessment);
//    }
//
//    // 3. UPDATE ASSESSMENT
//
//    @Override
//    @Transactional
//    public ClaimAssessmentResponseDto updateAssessment(
//            Long claimId,
//            ClaimAssessmentRequestDto requestDto) {
//
//        ClaimAssessment assessment = assessmentRepository
//                .findByClaim_ClaimId(claimId)
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        "Assessment not found"
//                ));
//
//        validateDocuments(claimId);
//        validateAmounts(requestDto);
//
//        BigDecimal netPayable = calculateNetPayable(requestDto);
//
//        assessment.setAssessedAmount(requestDto.getAssessedAmount());
//        assessment.setDeductible(requestDto.getDeductible());
//        assessment.setDepreciation(requestDto.getDepreciation());
//        assessment.setNonPayable(requestDto.getNonPayable());
//        assessment.setNetPayable(netPayable);
//
//        ClaimAssessment saved =
//                assessmentRepository.saveAndFlush(assessment);
//
//        historyService.recordHistory(
//                claimId,
//                "CLAIM_ASSESSMENT_UPDATED",
//                TEST_EMPLOYEE_ID,
//                "Updated net payable: " + netPayable
//        );
//
//        return toResponse(saved);
//    }
//
//    // 4. FIND CLAIM
//
//    private ClaimMaster findClaim(Long claimId) {
//
//        return claimRepository.findById(claimId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        "Claim not found: " + claimId
//                ));
//    }
//
//    // 5. VALIDATE DOCUMENTS
//
//    private void validateDocuments(Long claimId) {
//
//        List<ClaimDocuments> documents =
//                documentRepository.findByClaim_ClaimId(claimId);
//
//        if (documents.isEmpty()) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "No documents uploaded"
//            );
//        }
//
//        boolean unverified = documents.stream()
//                .anyMatch(document ->
//                        document.getVerificationStatus()
//                                != DocumentVerificationStatus.VERIFIED
//                );
//
//        if (unverified) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "All uploaded documents must be verified"
//            );
//        }
//    }
//
//    // 6. VALIDATE AMOUNTS
//
//    private void validateAmounts(ClaimAssessmentRequestDto dto) {
//
//        if (dto == null
//                || dto.getAssessedAmount() == null
//                || dto.getDeductible() == null
//                || dto.getDepreciation() == null
//                || dto.getNonPayable() == null) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "All assessment amounts are required"
//            );
//        }
//
//        if (dto.getAssessedAmount().signum() < 0
//                || dto.getDeductible().signum() < 0
//                || dto.getDepreciation().signum() < 0
//                || dto.getNonPayable().signum() < 0) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Amounts cannot be negative"
//            );
//        }
//    }
//
//    // 7. CALCULATE NET PAYABLE
//
//    private BigDecimal calculateNetPayable(
//            ClaimAssessmentRequestDto dto) {
//
//        BigDecimal netPayable = dto.getAssessedAmount()
//                .subtract(dto.getDeductible())
//                .subtract(dto.getDepreciation())
//                .subtract(dto.getNonPayable());
//
//        return netPayable.max(BigDecimal.ZERO);
//    }
//
//    // 8. ENTITY TO RESPONSE
//
//    private ClaimAssessmentResponseDto toResponse(
//            ClaimAssessment assessment) {
//
//        ClaimAssessmentResponseDto dto =
//                new ClaimAssessmentResponseDto();
//
//        dto.setAssessmentId(assessment.getAssessmentId());
//        dto.setClaimId(assessment.getClaim().getClaimId());
//        dto.setAssessedAmount(assessment.getAssessedAmount());
//        dto.setDeductible(assessment.getDeductible());
//        dto.setDepreciation(assessment.getDepreciation());
//        dto.setNonPayable(assessment.getNonPayable());
//        dto.setNetPayable(assessment.getNetPayable());
//
//        return dto;
//    }
//}



package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;

import org.example.insurance_policy_system.client.ProductCoverageClient;
import org.example.insurance_policy_system.dto.claimmanagement.*;
import org.example.insurance_policy_system.entity.claimmanagement.*;
import org.example.insurance_policy_system.enums.claimmanagement.DocumentVerificationStatus;
import org.example.insurance_policy_system.repository.claimmanagement.*;
import org.example.insurance_policy_system.service.claimmanagement.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimAssessmentServiceImpl
        implements ClaimAssessmentService {

    private static final Long TEST_EMPLOYEE_ID = 1L;

    private final ClaimMasterRepository claimRepository;
    private final ClaimAssessmentRepository assessmentRepository;
    private final ClaimDocumentsRepository documentRepository;
    private final ClaimHistoryService historyService;

    // NEW: PRODUCT SERVICE FEIGN CLIENT
    private final ProductCoverageClient productCoverageClient;

    // 1. CREATE ASSESSMENT

    @Override
    @Transactional
    public ClaimAssessmentResponseDto createAssessment(
            Long claimId,
            ClaimAssessmentRequestDto requestDto) {

        ClaimMaster claim = findClaim(claimId);

        if (assessmentRepository.existsByClaim_ClaimId(claimId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Assessment already exists"
            );
        }

        validateDocuments(claimId);
        validateAmounts(requestDto);

        // NEW: Fetch deductible from Product Service
        BigDecimal deductible =
                fetchDeductible(requestDto.getCoverageId());

        BigDecimal netPayable =
                calculateNetPayable(requestDto, deductible);

        ClaimAssessment assessment = new ClaimAssessment();

        assessment.setClaim(claim);
        assessment.setAssessedAmount(requestDto.getAssessedAmount());
        assessment.setDeductible(deductible);
//        assessment.setDepreciation(requestDto.getDepreciation());
//        assessment.setNonPayable(requestDto.getNonPayable());
        assessment.setNetPayable(netPayable);

        ClaimAssessment saved =
                assessmentRepository.saveAndFlush(assessment);

        historyService.recordHistory(
                claimId,
                "CLAIM_ASSESSED",
                TEST_EMPLOYEE_ID,
                "Deductible: " + deductible
                        + ", Net payable: " + netPayable
        );

        return toResponse(saved);
    }

    // 2. GET ASSESSMENT

    @Override
    @Transactional(readOnly = true)
    public ClaimAssessmentResponseDto getAssessment(Long claimId) {

        ClaimAssessment assessment = assessmentRepository
                .findByClaim_ClaimId(claimId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Assessment not found"
                ));

        return toResponse(assessment);
    }

    // 3. UPDATE ASSESSMENT

    @Override
    @Transactional
    public ClaimAssessmentResponseDto updateAssessment(
            Long claimId,
            ClaimAssessmentRequestDto requestDto) {

        ClaimAssessment assessment = assessmentRepository
                .findByClaim_ClaimId(claimId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Assessment not found"
                ));

        validateDocuments(claimId);
        validateAmounts(requestDto);

        // NEW: Fetch latest deductible from Product Service
        BigDecimal deductible =
                fetchDeductible(requestDto.getCoverageId());

        BigDecimal netPayable =
                calculateNetPayable(requestDto, deductible);

        assessment.setAssessedAmount(requestDto.getAssessedAmount());
        assessment.setDeductible(deductible);
     //   assessment.setDepreciation(requestDto.getDepreciation());
       // assessment.setNonPayable(requestDto.getNonPayable());
        assessment.setNetPayable(netPayable);

        ClaimAssessment saved =
                assessmentRepository.saveAndFlush(assessment);

        historyService.recordHistory(
                claimId,
                "CLAIM_ASSESSMENT_UPDATED",
                TEST_EMPLOYEE_ID,
                "Updated deductible: " + deductible
                        + ", Updated net payable: " + netPayable
        );

        return toResponse(saved);
    }

    // 4. FIND CLAIM

    private ClaimMaster findClaim(Long claimId) {

        return claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Claim not found: " + claimId
                ));
    }

    // 5. VALIDATE DOCUMENTS

    private void validateDocuments(Long claimId) {

        List<ClaimDocuments> documents =
                documentRepository.findByClaim_ClaimId(claimId);

        if (documents.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No documents uploaded"
            );
        }

        boolean unverified = documents.stream()
                .anyMatch(document ->
                        document.getVerificationStatus()
                                != DocumentVerificationStatus.VERIFIED
                );

        if (unverified) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "All uploaded documents must be verified"
            );
        }
    }

    // 6. VALIDATE AMOUNTS

    private void validateAmounts(ClaimAssessmentRequestDto dto) {

        if (dto == null
                || dto.getCoverageId() == null
                || dto.getAssessedAmount() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Coverage ID and assessment amounts are required"
            );
        }

        if (dto.getAssessedAmount().signum() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amounts cannot be negative"
            );
        }
    }

    // 7. FETCH DEDUCTIBLE USING FEIGN CLIENT

    private BigDecimal fetchDeductible(Long coverageId) {

        ProductCoverageResponseDto coverage;

        try {
            coverage = productCoverageClient
                    .getCoverageById(coverageId);

        } catch (FeignException.NotFound ex) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Coverage not found in Product Service: " + coverageId
            );

        } catch (RetryableException ex) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Product Service is unavailable",
                    ex
            );

        } catch (FeignException ex) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Product Service returned an error",
                    ex
            );
        }

        if (coverage == null
                || !coverageId.equals(coverage.getCoverageId())
                || coverage.getDeductibleAmount() == null
                || coverage.getDeductibleAmount().signum() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Invalid deductible received from Product Service"
            );
        }

        return coverage.getDeductibleAmount();
    }

    // 8. CALCULATE NET PAYABLE

    private BigDecimal calculateNetPayable(
            ClaimAssessmentRequestDto dto,
            BigDecimal deductible) {

        BigDecimal netPayable = dto.getAssessedAmount()
                .subtract(deductible);
//                .subtract(dto.getDepreciation())
//                .subtract(dto.getNonPayable());

        return netPayable.max(BigDecimal.ZERO);
    }

    // 9. ENTITY TO RESPONSE

    private ClaimAssessmentResponseDto toResponse(
            ClaimAssessment assessment) {

        ClaimAssessmentResponseDto dto =
                new ClaimAssessmentResponseDto();

        dto.setAssessmentId(assessment.getAssessmentId());
        dto.setClaimId(assessment.getClaim().getClaimId());
        dto.setAssessedAmount(assessment.getAssessedAmount());
        dto.setDeductible(assessment.getDeductible());
        dto.setDepreciation(assessment.getDepreciation());
        dto.setNonPayable(assessment.getNonPayable());
        dto.setNetPayable(assessment.getNetPayable());

        return dto;
    }
}

