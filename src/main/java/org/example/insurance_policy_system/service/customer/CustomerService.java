package org.example.insurance_policy_system.service.customer;

import org.example.insurance_policy_system.dto.customer.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    CustomerResponse registerCustomer(CustomerRegistrationRequest request);
    CustomerResponse getCustomerById(Long id);
    List<CustomerResponse> getAllCustomers();
    List<CustomerResponse> searchCustomers(String keyword);
    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request);
    void deleteCustomer(Long id);

    AddressResponse addAddress(Long customerId, AddressRequest request);
    AddressResponse updateAddress(Long addressId, AddressRequest request);
    void deleteAddress(Long addressId);
    List<AddressResponse> getAddressesByCustomer(Long customerId);

    NomineeResponse addNominee(Long customerId, NomineeRequest request);
    List<NomineeResponse> getNomineesByCustomer(Long customerId);
    NomineeResponse updateNominee(Long nomineeId, NomineeRequest request);
    void deleteNominee(Long nomineeId);

    DocumentResponse addDocument(Long customerId, String documentType, MultipartFile file);
    DocumentResponse updateDocument(Long documentId, String documentType, MultipartFile file);
    void deleteDocument(Long documentId);
}
