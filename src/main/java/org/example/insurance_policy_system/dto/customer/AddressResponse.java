package org.example.insurance_policy_system.dto.customer;

import lombok.Data;

@Data
public class AddressResponse {
    private Long addressId;
    private Long customerId;
    private String addressLine;
    private String addressLine2;
    private String city;
    private String district;
    private String state;
    private String country;
    private String pincode;
    private String addressType;
}
