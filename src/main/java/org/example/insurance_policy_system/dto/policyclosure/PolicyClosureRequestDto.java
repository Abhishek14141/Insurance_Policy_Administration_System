package org.example.policyclosureservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyClosureRequestDto {

    @NotNull(message = "Admin user ID is required")
    @Positive(message = "Admin user ID must be positive")
    private Long closedBy;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;
}
