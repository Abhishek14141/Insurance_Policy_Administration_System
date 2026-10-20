package org.example.insurance_policy_system.dto.policyservicing;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NomineeDetailsResponseDTO {

    private Long nomineeId;
    private Long policyId;
    private String nomineeName;
    private String relationship;
    private LocalDate dob;
    private BigDecimal sharePercentage;
    private String guardianName;
}

