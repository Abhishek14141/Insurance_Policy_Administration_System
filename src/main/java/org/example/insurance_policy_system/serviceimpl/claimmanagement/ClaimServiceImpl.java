//package org.example.insurance_policy_system.serviceimpl.claimmanagement;
//
//import feign.FeignException;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.insurance_policy_system.client.*;
//import org.example.insurance_policy_system.dto.claimmanagement.ClaimRequestDto;
//import org.example.insurance_policy_system.dto.claimmanagement.ClaimResponseDto;
//import org.example.insurance_policy_system.entity.claimmanagement.ClaimMaster;
//import org.example.insurance_policy_system.enums.claimmanagement.ClaimStatus;
//import org.example.insurance_policy_system.repository.claimmanagement.ClaimMasterRepository;
//import org.example.insurance_policy_system.service.claimmanagement.ClaimHistoryService;
//import org.example.insurance_policy_system.service.claimmanagement.ClaimService;
//import org.modelmapper.ModelMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//@Transactional
//@Slf4j
//public class ClaimServiceImpl  implements ClaimService {
//
//    private  final ModelMapper modelMapper;
//    private  final ClaimHistoryService historyService;
//    private  final ClaimMasterRepository claimRepository;
//
//    private  final CustomerClient customerClient;
//    private  final PolicyClient policyClient;
//    private  final PremiumClient premiumClient;
//
//
//
//
//    @Override
//    public ClaimResponseDto registerClaim(ClaimRequestDto requestDto) {
//
//        // Step 1: Validate request
//        validateRequest(requestDto);
//
//        // Step 2: Validate external microservice data
//        validateClaimEligibility(requestDto);
//
//        // Step 3: Map request to entity
////        ClaimMaster claim = modelMapper.map(
////                requestDto,
////                ClaimMaster.class
////        );
////
////        // Server-controlled fields
////        claim.setClaimId(null);
////        claim.setClaimNumber(null);
////        claim.setClaimDate(LocalDate.now());
////        claim.setClaimStatus(ClaimStatus.REGISTERED);
//
//        ModelMapper mapper = new ModelMapper();
//
//        mapper.typeMap(ClaimRequestDto.class, ClaimMaster.class)
//                .addMappings(mapping ->
//                        mapping.skip(ClaimMaster::setClaimId)
//                );
//
//        ClaimMaster claim = mapper.map(requestDto, ClaimMaster.class);
//
//        // Step 4: Save to generate database ID
//        ClaimMaster savedClaim =
//                claimRepository.saveAndFlush(claim);
//
//        // Step 5: Generate claim number
//        savedClaim.setClaimNumber(
//                String.format(
//                        "CLM-%010d",
//                        savedClaim.getClaimId()
//                )
//        );
//
//        // JPA dirty checking will persist claim number
//        // when the transaction commits.
//
//        // Step 6: Maintain history
//        historyService.recordHistory(
//                savedClaim.getClaimId(),
//                "CLAIM_REGISTERED",
//                getCurrentEmployeeId(),
//                "Claim registered successfully"
//        );
//
//        // Step 7: Return response
//        return toResponse(savedClaim);
//
//    }
//
//    @Override
//    public List<ClaimResponseDto> getAllClaims() {
//        return claimRepository.findAll()
//                .stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public ClaimResponseDto getClaimById(Long claimId) {
//        ClaimMaster claim = findClaim(claimId);
//
//        return toResponse(claim);
//    }
//
//    @Override
//    public ClaimResponseDto updateClaim(Long claimId, ClaimRequestDto requestDto) {// Step 1: Validate request
//        validateRequest(requestDto);
//
//        // Step 2: Fetch existing claim
//        ClaimMaster claim = findClaim(claimId);
//
//        // Step 3: Check status
//        if (claim.getClaimStatus() != ClaimStatus.REGISTERED) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Only registered claims can be updated"
//            );
//        }
//
//        // Step 4: Do not allow changing policy/customer
//        if (!Objects.equals(
//                claim.getPolicyId(),
//                requestDto.getPolicyId())
//                || !Objects.equals(
//                claim.getCustomerId(),
//                requestDto.getCustomerId())) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Policy ID and Customer ID cannot be changed"
//            );
//        }
//
//        // Step 5: Revalidate updated claim details
//        validateClaimEligibility(requestDto);
//
//        // Step 6: Update allowed fields
//        claim.setClaimType(requestDto.getClaimType());
//        claim.setIncidentDate(requestDto.getIncidentDate());
//        claim.setEstimatedLoss(requestDto.getEstimatedLoss());
//
//        // If ClaimMaster contains description:
//        // claim.setDescription(requestDto.getDescription());
//
//        // Step 7: Maintain history
//        historyService.recordHistory(
//                claimId,
//                "CLAIM_UPDATED",
//                getCurrentEmployeeId(),
//                "Claim details updated"
//        );
//
//        // JPA dirty checking updates the entity
//        return toResponse(claim);
//    }
//
//
//    @Override
//    public void cancelClaim(Long claimId) {
//        ClaimMaster claim = findClaim(claimId);
//
//        // Only before processing starts
//        if (claim.getClaimStatus() != ClaimStatus.REGISTERED) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Only registered claims can be cancelled"
//            );
//        }
//
//        // Add CANCELLED to ClaimStatus and database enum
//        // before enabling this operation.
//        claim.setClaimStatus(ClaimStatus.REJECTED);
//
//        historyService.recordHistory(
//                claimId,
//                "CLAIM_CANCELLED",
//                getCurrentEmployeeId(),
//                "Claim cancelled before processing"
//        );
//
//    }
//
//    private void validateClaimEligibility(
//            ClaimRequestDto requestDto) {
//
//        try {
//
//            // ---------------------------------------------
//            // A. CUSTOMER VALIDATION
//            // ---------------------------------------------
//
//            CustomerEligibilityDto customer =
//                    customerClient.getCustomerEligibility(
//                            requestDto.getCustomerId()
//                    );
//
//            if (customer == null
//                    || !Objects.equals(
//                    customer.getCustomerId(),
//                    requestDto.getCustomerId())
//                    || !Boolean.TRUE.equals(
//                    customer.getActive())) {
//
//                throw new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        "Customer is invalid or inactive"
//                );
//            }
//
//
//            // ---------------------------------------------
//            // B. POLICY VALIDATION
//            // ---------------------------------------------
//
//            PolicyEligibilityDto policy =
//                    policyClient.checkClaimEligibility(
//                            requestDto.getPolicyId(),
//                            requestDto.getCustomerId(),
//                            requestDto.getClaimType(),
//                            requestDto.getIncidentDate()
//                    );
//
//            if (policy == null
//                    || !Objects.equals(
//                    policy.getPolicyId(),
//                    requestDto.getPolicyId())
//                    || !Objects.equals(
//                    policy.getCustomerId(),
//                    requestDto.getCustomerId())
//                    || !Boolean.TRUE.equals(
//                    policy.getActive())
//                    || !Boolean.TRUE.equals(
//                    policy.getWithinCoveragePeriod())
//                    || !Boolean.TRUE.equals(
//                    policy.getWaitingPeriodCompleted())
//                    || !Boolean.TRUE.equals(
//                    policy.getClaimEligible())) {
//
//                throw new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        "Policy is not eligible for claim"
//                );
//            }
//
//
//            // ---------------------------------------------
//            // C. PREMIUM VALIDATION
//            // ---------------------------------------------
//
//            PremiumEligibilityDto premium =
//                    premiumClient.checkPremiumEligibility(
//                            requestDto.getPolicyId(),
//                            requestDto.getIncidentDate()
//                    );
//
//            if (premium == null
//                    || !Objects.equals(
//                    premium.getPolicyId(),
//                    requestDto.getPolicyId())
//                    || !Boolean.TRUE.equals(
//                    premium.getEligibleForClaim())) {
//
//                throw new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        "Premium payments are not eligible for claim"
//                );
//            }
//
////        } catch (FeignException.NotFound ex) {
////
////            throw new ResponseStatusException(
////                    HttpStatus.BAD_REQUEST,
////                    "Customer, Policy or Premium record not found",
////                    ex
////            );
////
////        }
//
//        } catch (FeignException.NotFound ex) {
//
//                log.error("Record not found. URL: {}, Response: {}",
//                        ex.request().url(),
//                        ex.contentUTF8(), ex);
//
//                throw new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        "Customer, Policy or Premium record not found"
//                );
//
//            } catch (FeignException ex) {
//
//                log.error("Feign call failed. Status: {}, URL: {}, Response: {}",
//                        ex.status(),
//                        ex.request().url(),
//                        ex.contentUTF8(),
//                        ex);
//
//                throw new ResponseStatusException(
//                        HttpStatus.SERVICE_UNAVAILABLE,
//                        "Customer, Policy or Premium service unavailable"
//                );
//
//        }
//    }
//
//
//    // =====================================================
//    // 7. VALIDATE CLAIM REQUEST
//    // =====================================================
//
//    private void validateRequest(ClaimRequestDto requestDto) {
//
//        if (requestDto == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Claim request cannot be null"
//            );
//        }
//
//        if (requestDto.getPolicyId() == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Policy ID is required"
//            );
//        }
//
//        if (requestDto.getCustomerId() == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Customer ID is required"
//            );
//        }
//
//        if (requestDto.getClaimType() == null
//                || requestDto.getClaimType().trim().isEmpty()) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Claim type is required"
//            );
//        }
//
//        if (requestDto.getDescription() == null
//                || requestDto.getDescription().trim().isEmpty()) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Claim description is required"
//            );
//        }
//
//        if (requestDto.getIncidentDate() == null) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Incident date is required"
//            );
//        }
//
//        if (requestDto.getIncidentDate()
//                .isAfter(LocalDate.now())) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Incident date cannot be in the future"
//            );
//        }
//
//        if (requestDto.getEstimatedLoss() == null
//                || requestDto.getEstimatedLoss()
//                .compareTo(BigDecimal.ZERO) <= 0) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Estimated loss must be greater than zero"
//            );
//        }
//    }
//
//
//    // =====================================================
//    // 8. FIND CLAIM
//    // =====================================================
//
//    private ClaimMaster findClaim(Long claimId) {
//
//        if (claimId == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Claim ID is required"
//            );
//        }
//
//        return claimRepository.findById(claimId)
//                .orElseThrow(() ->
//                        new ResponseStatusException(
//                                HttpStatus.NOT_FOUND,
//                                "Claim not found: " + claimId
//                        )
//                );
//    }
//
//
//    // =====================================================
//    // 9. ENTITY -> RESPONSE DTO
//    // =====================================================
//
//    private ClaimResponseDto toResponse(ClaimMaster claim) {
//
//        return modelMapper.map(
//                claim,
//                ClaimResponseDto.class
//        );
//    }
//
//
//    // =====================================================
//    // 10. CURRENT AUTHENTICATED EMPLOYEE
//    // =====================================================
//
//    private Long getCurrentEmployeeId() {
//
//        // TODO: Resolve the employee ID from your
//        // authenticated JWT/Spring Security principal.
//        //
//        // Do not use 0L: performed_by is an Employee FK.
//        //
//        // This intentionally fails until actor resolution
//        // is connected, rather than writing a fake audit ID.
//
//        throw new ResponseStatusException(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "Authenticated employee ID resolver not configured"
//        );
//    }
//
//
//
//
//}
package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.insurance_policy_system.client.*;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimRequestDto;
import org.example.insurance_policy_system.dto.claimmanagement.ClaimResponseDto;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimHistory;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimMaster;
import org.example.insurance_policy_system.enums.claimmanagement.ClaimStatus;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimMasterRepository;
import org.example.insurance_policy_system.service.claimmanagement.ClaimHistoryService;
import org.example.insurance_policy_system.service.claimmanagement.ClaimService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClaimServiceImpl implements ClaimService {

    private final ModelMapper modelMapper;
    private final ClaimMasterRepository claimRepository;
        private  final ClaimHistoryService historyService;
    private final CustomerClient customerClient;
    private final PolicyClient policyClient;
    private final PremiumClient premiumClient;

    // =====================================
    // 1. REGISTER CLAIM
    // =====================================

   Long  employeeId = 1L;

    @Override
    public ClaimResponseDto registerClaim(ClaimRequestDto requestDto) {

        // Validate request
        validateRequest(requestDto);

        // Validate external services
        validateClaimEligibility(requestDto);


        // Step 2: Check duplicate claim
        boolean alreadyExists =
                claimRepository.existsByCustomerIdAndClaimType(
                        requestDto.getCustomerId(),
                        requestDto.getClaimType()
                );

        if (alreadyExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Customer has already registered a claim of type: "
                            + requestDto.getClaimType()
            );
        }



        // Manual mapping - avoids ModelMapper errors
        ClaimMaster claim = new ClaimMaster();

        claim.setCustomerId(requestDto.getCustomerId());
        claim.setPolicyId(requestDto.getPolicyId());
        claim.setClaimType(requestDto.getClaimType());
        claim.setIncidentDate(requestDto.getIncidentDate());
        claim.setEstimatedLoss(requestDto.getEstimatedLoss());

        // Server controlled fields
        claim.setClaimId(null);
        claim.setClaimNumber(null);
        claim.setClaimDate(LocalDate.now());
        claim.setClaimStatus(ClaimStatus.REGISTERED);

        // Save claim and generate database ID
        ClaimMaster savedClaim =
                claimRepository.save(claim);

        // Generate claim number
        String claimNumber = String.format(
                "CLM-%010d",
                savedClaim.getClaimId()
        );

        savedClaim.setClaimNumber(claimNumber);

        // Save updated claim number
        savedClaim = claimRepository.save(savedClaim);

        // History temporarily disabled.
        // No employee ID required for testing.

        historyService.recordHistory(
                savedClaim.getClaimId(),
                "CLAIM_REGISTERED",
                employeeId,
                "Claim registered successfully"
        );

        log.info(
                "Claim registered successfully: ID={}, Number={}",
                savedClaim.getClaimId(),
                savedClaim.getClaimNumber()
        );

        return toResponse(savedClaim);
    }

    // =====================================
    // 2. GET ALL CLAIMS
    // =====================================

    @Override
    public List<ClaimResponseDto> getAllClaims() {

        return claimRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================
    // 3. GET CLAIM BY ID
    // =====================================

    @Override
    public ClaimResponseDto getClaimById(Long claimId) {

        ClaimMaster claim = findClaim(claimId);

        return toResponse(claim);
    }

    // =====================================
    // 4. UPDATE CLAIM
    // =====================================

    @Override
    public ClaimResponseDto updateClaim(
            Long claimId,
            ClaimRequestDto requestDto) {

        validateRequest(requestDto);

        ClaimMaster claim = findClaim(claimId);

        if (claim.getClaimStatus() != ClaimStatus.REGISTERED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only registered claims can be updated"
            );
        }

        // Customer and policy cannot be changed
        if (!Objects.equals(
                claim.getPolicyId(),
                requestDto.getPolicyId())
                || !Objects.equals(
                claim.getCustomerId(),
                requestDto.getCustomerId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Policy ID and Customer ID cannot be changed"
            );
        }

        validateClaimEligibility(requestDto);

        claim.setClaimType(requestDto.getClaimType());
        claim.setIncidentDate(requestDto.getIncidentDate());
        claim.setEstimatedLoss(requestDto.getEstimatedLoss());

        ClaimMaster updatedClaim =
                claimRepository.saveAndFlush(claim);

        // History temporarily disabled

        return toResponse(updatedClaim);
    }

    // =====================================
    // 5. CANCEL CLAIM
    // =====================================

    @Override
    public void cancelClaim(Long claimId) {

        ClaimMaster claim = findClaim(claimId);

        if (claim.getClaimStatus() != ClaimStatus.REGISTERED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only registered claims can be cancelled"
            );
        }

        // Existing enum does not have CANCELLED
        claim.setClaimStatus(ClaimStatus.REJECTED);

        claimRepository.saveAndFlush(claim);

        // History temporarily disabled
    }

    // =====================================
    // 6. VALIDATE CLAIM ELIGIBILITY
    // =====================================

    private void validateClaimEligibility(
            ClaimRequestDto requestDto) {

        try {

            // CUSTOMER VALIDATION

            CustomerEligibilityDto customer =
                    customerClient.getCustomerEligibility(
                            requestDto.getCustomerId()
                    );

            if (customer == null
                    || !Objects.equals(
                    customer.getCustomerId(),
                    requestDto.getCustomerId())
                    || !Boolean.TRUE.equals(
                    customer.getActive())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Customer is invalid or inactive"
                );
            }

            // POLICY VALIDATION

            PolicyEligibilityDto policy =
                    policyClient.checkClaimEligibility(
                            requestDto.getPolicyId(),
                            requestDto.getCustomerId(),
                            requestDto.getClaimType(),
                            requestDto.getIncidentDate()
                    );

            if (policy == null
                    || !Objects.equals(
                    policy.getPolicyId(),
                    requestDto.getPolicyId())
                    || !Objects.equals(
                    policy.getCustomerId(),
                    requestDto.getCustomerId())
                    || !Boolean.TRUE.equals(
                    policy.getActive())
                    || !Boolean.TRUE.equals(
                    policy.getWithinCoveragePeriod())
                    || !Boolean.TRUE.equals(
                    policy.getWaitingPeriodCompleted())
                    || !Boolean.TRUE.equals(
                    policy.getClaimEligible())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Policy is not eligible for claim"
                );
            }

            // PREMIUM VALIDATION

            PremiumEligibilityDto premium =
                    premiumClient.checkPremiumEligibility(
                            requestDto.getPolicyId(),
                            requestDto.getIncidentDate()
                    );

            if (premium == null
                    || !Objects.equals(
                    premium.getPolicyId(),
                    requestDto.getPolicyId())
                    || !Boolean.TRUE.equals(
                    premium.getEligibleForClaim())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Premium payments are not eligible for claim"
                );
            }

        } catch (FeignException.NotFound ex) {

            log.error(
                    "External record not found. URL: {}, Response: {}",
                    ex.request().url(),
                    ex.contentUTF8(),
                    ex
            );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Customer, Policy or Premium record not found",
                    ex
            );

        } catch (FeignException ex) {

            log.error(
                    "Feign error. Status: {}, URL: {}, Response: {}",
                    ex.status(),
                    ex.request().url(),
                    ex.contentUTF8(),
                    ex
            );

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Customer, Policy or Premium service unavailable",
                    ex
            );
        }
    }

    // =====================================
    // 7. VALIDATE REQUEST
    // =====================================

    private void validateRequest(ClaimRequestDto requestDto) {

        if (requestDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Claim request cannot be null"
            );
        }

        if (requestDto.getPolicyId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Policy ID is required"
            );
        }

        if (requestDto.getCustomerId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Customer ID is required"
            );
        }

        if (requestDto.getClaimType() == null
                || requestDto.getClaimType().trim().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Claim type is required"
            );
        }

        if (requestDto.getDescription() == null
                || requestDto.getDescription().trim().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Claim description is required"
            );
        }

        if (requestDto.getIncidentDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Incident date is required"
            );
        }

        if (requestDto.getIncidentDate()
                .isAfter(LocalDate.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Incident date cannot be in the future"
            );
        }

        if (requestDto.getEstimatedLoss() == null
                || requestDto.getEstimatedLoss()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estimated loss must be greater than zero"
            );
        }
    }

    // =====================================
    // 8. FIND CLAIM
    // =====================================

    private ClaimMaster findClaim(Long claimId) {

        if (claimId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Claim ID is required"
            );
        }

        return claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Claim not found: " + claimId
                        )
                );
    }

    // =====================================
    // 9. CONVERT ENTITY TO RESPONSE DTO
    // =====================================

    private ClaimResponseDto toResponse(ClaimMaster claim) {

        return modelMapper.map(
                claim,
                ClaimResponseDto.class
        );
    }
}

