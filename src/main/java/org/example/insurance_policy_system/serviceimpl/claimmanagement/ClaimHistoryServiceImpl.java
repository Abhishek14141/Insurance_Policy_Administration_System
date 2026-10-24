package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimHistory;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimMaster;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimHistoryRepository;
import org.example.insurance_policy_system.repository.claimmanagement.ClaimMasterRepository;
import org.example.insurance_policy_system.service.claimmanagement.ClaimHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class ClaimHistoryServiceImpl implements ClaimHistoryService {

    private final ClaimHistoryRepository claimHistoryRepository;
    private final ClaimMasterRepository claimRepository;

//    public ClaimHistoryServiceImpl(
//            ClaimHistoryRepository claimHistoryRepository,
//            ClaimMasterRepository claimRepository) {
//
//        this.claimHistoryRepository = claimHistoryRepository;
//        this.claimRepository = claimRepository;
//    }

    @Override
    @Transactional
    public void recordHistory(
            Long claimId,
            String action,
            Long performedBy,
            String remarks
    ) {

        ClaimMaster claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Claim not found: " + claimId
                ));

        ClaimHistory history = new ClaimHistory();

        history.setClaim(claim);
        history.setAction(action);
        history.setPerformedBy(performedBy);
        history.setRemarks(remarks);
        history.setActionDate(LocalDateTime.now());

        claimHistoryRepository.save(history);
    }
}