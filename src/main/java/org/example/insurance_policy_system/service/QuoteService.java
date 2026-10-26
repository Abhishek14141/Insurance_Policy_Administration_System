package org.example.insurance_policy_system.service;

import org.example.insurance_policy_system.dto.*;
import org.example.insurance_policy_system.entity.ApiResponse;
import java.util.List;

public interface QuoteService {
    ApiResponse<QuoteResponseDto> createQuote(QuoteRequestDto request);
    ApiResponse<QuoteResponseDto> getQuote(Long quoteId);
    ApiResponse<List<QuoteResponseDto>> getAllQuotes();
    ApiResponse<QuoteResponseDto> updateQuote(Long quoteId, QuoteRequestDto request);
    ApiResponse<QuoteResponseDto> approveQuote(Long quoteId);
    ApiResponse<QuoteResponseDto> rejectQuote(Long quoteId, String remarks);
    ApiResponse<PremiumCalculationResponseDto> calculatePremium(PremiumCalculationRequestDto request);
    PremiumCalculationResponseDto getPremium(Long quoteId);
    ApiResponse<List<QuoteHistoryResponseDto>> getHistory(Long quoteId);
}
