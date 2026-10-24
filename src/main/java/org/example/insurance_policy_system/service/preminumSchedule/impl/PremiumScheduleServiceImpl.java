package org.example.insurance_policy_system.service.preminumSchedule.impl;

import com.ampta.premiumservice.dto.ScheduleRequestDto;
import com.ampta.premiumservice.dto.ScheduleResponseDto;
import com.ampta.premiumservice.dto.ScheduleUpdateDto;
import com.ampta.premiumservice.entity.PremiumSchedule;
import com.ampta.premiumservice.entity.enums.InstallmentStatus;
import com.ampta.premiumservice.exception.ResourceNotFoundException;
import com.ampta.premiumservice.repository.PremiumScheduleRepository;
import com.ampta.premiumservice.service.PremiumScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PremiumScheduleServiceImpl implements PremiumScheduleService {

    private final PremiumScheduleRepository scheduleRepository;

    @Value("${premium.penalty-per-day:10}") // 10/day
    private BigDecimal penaltyPerDay;

    @Override
    public List<ScheduleResponseDto> generateSchedule(ScheduleRequestDto request) {

        int count = switch (request.getPremiumFrequency().toUpperCase()){
            case "MONTHLY" -> request.getPolicyTermMonths();
            case "QUARTERLY" -> request.getPolicyTermMonths() / 3;
            case "HALF_YEARLY" -> request.getPolicyTermMonths() / 6;
            case "YEARLY" -> request.getPolicyTermMonths() / 12;
            default -> throw new ResourceNotFoundException("Invalid frequency: " + request.getPremiumFrequency());
        };

        BigDecimal installmentAmount = request.getFinalPremium()
                .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);

        List<PremiumSchedule> schedules = new ArrayList<>();

        for(int i = 1; i<= count; i++){
            PremiumSchedule schedule = new PremiumSchedule();
            schedule.setPolicyId(request.getPolicyId());
            schedule.setInstallmentNo(i);
            schedule.setPremiumAmount(installmentAmount);
            schedule.setDueDate(request.getStartDate().plusMonths((long) i * (request.getPolicyTermMonths() / count)));
            schedule.setStatus(InstallmentStatus.PENDING);
            schedules.add(schedule);
        }

        return scheduleRepository.saveAll(schedules)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<ScheduleResponseDto> getScheduleByPolicy(Long policyId) {
        return scheduleRepository.findByPolicyIdOrderByInstallmentNoAsc(policyId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto updateInstallment(Long scheduleId, ScheduleUpdateDto dto) {
        return null;
    }

    private ScheduleResponseDto toDto(PremiumSchedule s) {
        return ScheduleResponseDto.builder()
                .scheduleId(s.getScheduleId())
                .policyId(s.getPolicyId())
                .installmentNo(s.getInstallmentNo())
                .dueDate(s.getDueDate())
                .premiumAmount(s.getPremiumAmount())
                .lateFee(s.getLateFee())
                .totalPayable(s.getPremiumAmount().add(s.getLateFee()))
                .status(s.getStatus().name())
                .paidDate(s.getPaidDate())
                .createdAt(s.getCreatedAt())
                .build();
    }

}












/*
* @Override
    @Transactional
    public List<ScheduleResponseDto> generateSchedule(
            ScheduleRequestDto request) {

        if (request.getPolicyId() == null) {
            throw new IllegalArgumentException(
                    "Policy ID is required"
            );
        }

        // 1. Fetch policy from Policy Service
        PolicyDto policy =
                policyClient.getPolicy(request.getPolicyId());

        if (policy == null
                || policy.getEffectiveDate() == null
                || policy.getExpiryDate() == null
                || policy.getFinalPremium() == null
                || policy.getPremiumFrequency() == null) {
            throw new IllegalArgumentException(
                    "Policy details are incomplete"
            );
        }

        LocalDate startDate = policy.getEffectiveDate();
        LocalDate expiryDate = policy.getExpiryDate();
        BigDecimal totalPremium = policy.getFinalPremium();

        if (!expiryDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "Expiry date must be after effective date"
            );
        }

        if (totalPremium.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Premium must be greater than zero"
            );
        }

        // 2. Determine installment interval
        int intervalMonths = switch (
                policy.getPremiumFrequency().toUpperCase()) {
            case "MONTHLY" -> 1;
            case "QUARTERLY" -> 3;
            case "HALF_YEARLY" -> 6;
            case "YEARLY" -> 12;
            default -> throw new IllegalArgumentException(
                    "Unsupported premium frequency"
            );
        };

        // 3. Calculate number of installments
        int totalMonths = Period.between(
                startDate, expiryDate
        ).getMonths()
          + (Period.between(startDate, expiryDate).getYears() * 12);

        if (totalMonths <= 0) {
            throw new IllegalArgumentException(
                    "Policy term must be at least one month"
            );
        }

        int installmentCount =
                (int) Math.ceil(
                        (double) totalMonths / intervalMonths
                );

        BigDecimal installmentAmount = totalPremium.divide(
                BigDecimal.valueOf(installmentCount),
                2,
                RoundingMode.HALF_UP
        );

        // 4. Generate schedule
        List<PremiumSchedule> schedules = new ArrayList<>();

        for (int i = 1; i <= installmentCount; i++) {

            PremiumSchedule schedule = new PremiumSchedule();

            schedule.setPolicyId(policy.getPolicyId());
            schedule.setCustomerId(policy.getCustomerId());
            schedule.setInstallmentNo(i);

            LocalDate dueDate = startDate.plusMonths(
                    (long) i * intervalMonths
            );

            if (dueDate.isAfter(expiryDate)) {
                dueDate = expiryDate;
            }

            schedule.setDueDate(dueDate);

            // Adjust the last installment to avoid rounding differences
            BigDecimal amount = i == installmentCount
                    ? totalPremium.subtract(
                        installmentAmount.multiply(
                                BigDecimal.valueOf(installmentCount - 1)
                        )
                    )
                    : installmentAmount;

            schedule.setPremiumAmount(amount);
            schedule.setLateFee(BigDecimal.ZERO);
            schedule.setStatus(ScheduleStatus.PENDING);

            schedules.add(schedule);
        }

        // 5. Save in database
        return scheduleRepository.saveAll(schedules)
                .stream()
                .map(schedule -> new ScheduleResponseDto(
                        schedule.getScheduleId(),
                        schedule.getPolicyId(),
                        schedule.getInstallmentNo(),
                        schedule.getDueDate(),
                        schedule.getPremiumAmount(),
                        schedule.getStatus().name()
                ))
                .toList();
    }*/