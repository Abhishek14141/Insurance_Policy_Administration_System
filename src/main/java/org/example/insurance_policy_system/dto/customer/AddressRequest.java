package org.example.insurance_policy_system.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank private String addressLine;
    private String addressLine2;
    @NotBlank private String city;
    @NotBlank private String district;
    @NotBlank private String state;
    @NotBlank private String country;
    @NotBlank @Pattern(regexp = "^[0-9]{5,10}$", message = "Pincode must contain 5 to 10 digits")
    private String pincode;
    private String addressType;
}
