package org.example.insurance_policy_system.dto.undertakingDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.UnderwritingStatus;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingResponseDto {

    private Long underwritingId;
    private Long quoteId;
    private Long customerId;
    private Long productId;
    private LocalDate underwritingDate;
    private UnderwritingStatus status;

}
