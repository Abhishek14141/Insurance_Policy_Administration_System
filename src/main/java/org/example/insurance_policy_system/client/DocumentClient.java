package org.example.insurance_policy_system.client;

//import org.example.insurance_policy_system.dto.claimmanagement.client.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "document-service", url = "${services.document.url}")
public interface DocumentClient {

    @PostMapping("/internal/documents/scan")
    DocumentScanResponseDto scanDocument(
            @RequestBody DocumentScanRequestDto request);

    @GetMapping("/internal/documents/claims/{claimType}/requirements")
    RequiredDocumentsDto getRequiredDocuments(
            @PathVariable("claimType") String claimType);
}