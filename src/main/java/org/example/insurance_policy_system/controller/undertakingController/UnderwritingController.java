package org.example.insurance_policy_system.controller.undertakingController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.undertakingDto.MedicalUnderwritingRequestDto;
import org.example.insurance_policy_system.dto.undertakingDto.UnderwritingRequestDto;
import org.example.insurance_policy_system.dto.undertakingDto.UnderwritingResponseDto;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.undertakingService.UnderwritingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UnderwritingController {

    private final UnderwritingService underwritingService;

    @PostMapping("/cases")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> createCase(
            @Valid @RequestBody UnderwritingRequestDto requestDto) {
        ApiResponse<UnderwritingResponseDto> response = underwritingService.createCase(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{caseId}")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> getCaseById(@PathVariable Long caseId){
        return  ResponseEntity.ok(underwritingService.getCaseById(caseId));
    }
    @GetMapping("/cases")
    public ResponseEntity<ApiResponse<List<UnderwritingResponseDto>>> getAllCases(){
        return  ResponseEntity.ok(underwritingService.getAllCase());
    }
    @PostMapping("/cases/{caseId}/medical-assessment")
    public ResponseEntity<ApiResponse<UnderwritingResponseDto>> submitMedicalAssessment(@PathVariable Long caseId, @Valid @RequestBody MedicalUnderwritingRequestDto requestDto){
        ApiResponse<UnderwritingResponseDto> response=underwritingService.submitMedicalAssessment(caseId,requestDto);
        return  ResponseEntity.ok(response);
    }
}
