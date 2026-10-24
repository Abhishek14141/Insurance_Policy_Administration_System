package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.QuoteRiskAssessmentRequestDto;
import org.example.insurance_policy_system.dto.QuoteRiskAssessmentResponseDto;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.QuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/risk-assessment")
@RequiredArgsConstructor
@Slf4j
public class QuoteRiskController {
    private final QuoteService quoteService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuoteRiskAssessmentResponseDto>> assessRisk(@Valid @RequestBody QuoteRiskAssessmentRequestDto request) {
        log.info("POST quote risk assessment quoteId={}", request.getQuoteId());
        return ResponseEntity.ok(quoteService.assessRisk(request));
    }
    @GetMapping("/{quoteId}")
    public ResponseEntity<ApiResponse<QuoteRiskAssessmentResponseDto>> getRisk(@PathVariable Long quoteId) {
        log.info("GET quote risk quoteId={}", quoteId); return ResponseEntity.ok(quoteService.getRisk(quoteId));
    }
}
