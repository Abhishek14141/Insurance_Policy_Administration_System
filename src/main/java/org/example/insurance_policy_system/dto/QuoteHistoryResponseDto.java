package org.example.insurance_policy_system.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuoteHistoryResponseDto {
    private Long historyId;
    private Long quoteId;
    private String action;
    private Long performedBy;
    private LocalDateTime actionDate;
    private String remarks;
}
