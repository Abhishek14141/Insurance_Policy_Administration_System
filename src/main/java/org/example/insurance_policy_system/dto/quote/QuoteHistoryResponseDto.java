package org.example.insurance_policy_system.dto.quote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteHistoryResponseDto {

    private Long historyId;
    private Long quoteId;
    private String action;
    private Long performedBy;
    private LocalDateTime actionDate;
    private String remark;
}