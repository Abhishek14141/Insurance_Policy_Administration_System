package org.example.insurance_policy_system.service.preminumSchedule;

import com.ampta.premiumservice.dto.ScheduleRequestDto;
import com.ampta.premiumservice.dto.ScheduleResponseDto;
import com.ampta.premiumservice.dto.ScheduleUpdateDto;

import java.util.List;

public interface PremiumScheduleService {
    List<ScheduleResponseDto> generateSchedule(ScheduleRequestDto request);  // called on policy issuance
    List<ScheduleResponseDto> getScheduleByPolicy(Long policyId);
    ScheduleResponseDto updateInstallment(Long scheduleId, ScheduleUpdateDto dto);
}
