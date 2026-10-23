package org.example.insurance_policy_system.client;

import lombok.Data;

import java.util.List;
@Data
public class RequiredDocumentsDto {

    private String claimType;
    private List<String> requiredDocumentNames;

}
