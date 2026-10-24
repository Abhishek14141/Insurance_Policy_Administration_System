package org.example.insurance_policy_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import org.example.insurance_policy_system.service.QuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
@Slf4j
public class QuoteController {
    private final QuoteService quoteService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuoteResponseDto>> createQuote(@Valid @RequestBody QuoteRequestDto request) {
        log.info("POST create quote customerId={}", request.getCustomerId());
        return ResponseEntity.ok(quoteService.createQuote(request));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuoteResponseDto>>> getAllQuotes() {
        log.info("GET all quotes"); return ResponseEntity.ok(quoteService.getAllQuotes());
    }
    @GetMapping("/{quoteId}")
    public ResponseEntity<ApiResponse<QuoteResponseDto>> getQuote(@PathVariable Long quoteId) {
        log.info("GET quote quoteId={}", quoteId); return ResponseEntity.ok(quoteService.getQuote(quoteId));
    }
    @PutMapping("/{quoteId}")
    public ResponseEntity<ApiResponse<QuoteResponseDto>> updateQuote(@PathVariable Long quoteId, @Valid @RequestBody QuoteRequestDto request) {
        log.info("PUT quote quoteId={}", quoteId); return ResponseEntity.ok(quoteService.updateQuote(quoteId, request));
    }
    @PutMapping("/{quoteId}/approve")
    public ResponseEntity<ApiResponse<QuoteResponseDto>> approveQuote(@PathVariable Long quoteId) {
        log.info("PUT approve quote quoteId={}", quoteId); return ResponseEntity.ok(quoteService.approveQuote(quoteId));
    }
    @PutMapping("/{quoteId}/reject")
    public ResponseEntity<ApiResponse<QuoteResponseDto>> rejectQuote(@PathVariable Long quoteId, @RequestParam(required = false) String remarks) {
        log.info("PUT reject quote quoteId={}", quoteId); return ResponseEntity.ok(quoteService.rejectQuote(quoteId, remarks));
    }
    @PostMapping("/premium-calculation")
    public ResponseEntity<ApiResponse<PremiumCalculationResponseDto>> calculatePremium(@Valid @RequestBody PremiumCalculationRequestDto request) {
        log.info("POST premium calculation quoteId={}", request.getQuoteId()); return ResponseEntity.ok(quoteService.calculatePremium(request));
    }
    @GetMapping("/{id}/premium")
    public ApiResponse<PremiumCalculationResponseDto> premium(@PathVariable Long id) {
        return new ApiResponse<>(true, "Premium details fetched", quoteService.getPremium(id), LocalDateTime.now());
    }
    @GetMapping("/{quoteId}/history")
    public ResponseEntity<ApiResponse<List<QuoteHistoryResponseDto>>> getHistory(@PathVariable Long quoteId) {
        log.info("GET quote history quoteId={}", quoteId); return ResponseEntity.ok(quoteService.getHistory(quoteId));
    }


}
