package org.example.insurance_policy_system.controller.customer;

import jakarta.validation.Valid;

import org.example.insurance_policy_system.dto.customer.*;
import org.example.insurance_policy_system.service.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService service;
    public CustomerController(CustomerService service) {
        this.service = service;
    }
    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerCustomer(request));
    }

    @GetMapping("/customers/{id}")
    public CustomerResponse getCustomer(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    @GetMapping("/customers")
    public List<CustomerResponse> getCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerResponse> searchCustomers(@RequestParam(required = false) String keyword) {
        return service.searchCustomers(keyword);
    }

    @PutMapping("/customers/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request) {
        return service.updateCustomer(id, request);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customers/{id}/addresses")
    public ResponseEntity<AddressResponse> addAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addAddress(id, request));
    }

    @GetMapping("/customers/{id}/addresses")
    public List<AddressResponse> getCustomerAddresses(@PathVariable Long id) {
        return service.getAddressesByCustomer(id);
    }

    @PutMapping("/addresses/{id}")
    public AddressResponse updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        return service.updateAddress(id, request);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        service.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customers/{id}/nominees")
    public ResponseEntity<NomineeResponse> addNominee(
            @PathVariable Long id,
            @Valid @RequestBody NomineeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addNominee(id, request));
    }

    @GetMapping("/customers/{id}/nominees")
    public List<NomineeResponse> getCustomerNominees(@PathVariable Long id) {
        return service.getNomineesByCustomer(id);
    }

    @PutMapping("/nominees/{id}")
    public NomineeResponse updateNominee(
            @PathVariable Long id,
            @Valid @RequestBody NomineeRequest request) {
        return service.updateNominee(id, request);
    }

    @DeleteMapping("/nominees/{id}")
    public ResponseEntity<Void> deleteNominee(@PathVariable Long id) {
        service.deleteNominee(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/customers/{id}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> addDocument(
            @PathVariable Long id,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        DocumentResponse response = service.addDocument(id, documentType, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(
            value = "/documents/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DocumentResponse updateDocument(
            @PathVariable Long id,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        return service.updateDocument(id, documentType, file);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
