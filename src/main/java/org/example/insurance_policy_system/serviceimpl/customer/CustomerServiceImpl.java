package org.example.insurance_policy_system.serviceimpl.customer;


import org.example.insurance_policy_system.dto.customer.*;
import org.example.insurance_policy_system.entity.customer.*;
import org.example.insurance_policy_system.repository.customer.*;
import org.example.insurance_policy_system.service.customer.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Value("${app.document.max-size-bytes:5242880}")
    private long maxFileSize;
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".pdf", ".jpg", ".jpeg", ".png");
    private static final List<String> ALLOWED_DOCUMENT_TYPES = List.of(
            "AADHAAR_CARD", "DRIVING_LICENSE", "PAN_CARD", "PASSPORT_SIZE_PHOTO");

    private final CustomerRepository customers;
    private final CustomerAddressRepository addresses;
    private final NomineeRepository nominees;
    private final CustomerDocumentRepository documents;
    private final AuditLogRepository auditLogs;
    private final ModelMapper modelMapper;

    @Value("${app.document.upload-dir:uploads/documents}")
    private String uploadDir;

    public CustomerServiceImpl(
            CustomerRepository customers,
            CustomerAddressRepository addresses,
            NomineeRepository nominees,
            CustomerDocumentRepository documents,
            AuditLogRepository auditLogs,
            ModelMapper modelMapper) {
        this.customers = customers;
        this.addresses = addresses;
        this.nominees = nominees;
        this.documents = documents;
        this.auditLogs = auditLogs;
        this.modelMapper = modelMapper;
    }

    private Customer customer(Long id) {
        return customers.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    private CustomerAddress address(Long id) {
        return addresses.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
    }

    private Nominee nominee(Long id) {
        return nominees.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nominee not found: " + id));
    }

    private CustomerDocument document(Long id) {
        return documents.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + id));
    }

    private void audit(Long customerId, String entity, Long entityId, String action, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setCustomerId(customerId);
        auditLog.setEntityName(entity);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLogs.save(auditLog);
    }

    private String generateCustomerCode() {
        return "CUS" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 12).toUpperCase(Locale.ROOT);
    }

    @Override
    public CustomerResponse registerCustomer(CustomerRegistrationRequest request) {
        boolean duplicate = customers.findAll().stream().anyMatch(existing ->
                existing.getEmail().equalsIgnoreCase(request.getEmail())
                        || existing.getMobileNumber().equals(request.getMobileNumber()));
        if (duplicate) {
            throw new IllegalArgumentException("Email or mobile number already registered");
        }

        Customer customer = new Customer();
        customer.setCustomerCode(generateCustomerCode());
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setEmail(request.getEmail());
        customer.setCustomerStatus("ACTIVE");

        CustomerOccupation occupationDetails = new CustomerOccupation();
        occupationDetails.setCustomer(customer);
        occupationDetails.setOccupation(request.getOccupation());
        occupationDetails.setEmployerName(request.getEmployerName());
        occupationDetails.setEmploymentType(request.getEmploymentType());
        occupationDetails.setAnnualIncome(request.getAnnualIncome());
        customer.setOccupationDetails(occupationDetails);

        CustomerContact contact = new CustomerContact();
        contact.setCustomer(customer);
        contact.setPreferredContactMode(request.getPreferredContactMode().name());
        customer.setContact(contact);

        customer = customers.save(customer);
        audit(customer.getCustomerId(), "Customer", customer.getCustomerId(), "CREATE", "Customer registered");
        return map(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        return map(customer(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customers.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> searchCustomers(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllCustomers();
        }
        String query = keyword.toLowerCase(Locale.ROOT);
        return customers.findAll().stream()
                .filter(c -> contains(c.getCustomerCode(), query)
                        || contains(c.getFirstName(), query)
                        || contains(c.getLastName(), query)
                        || contains(c.getEmail(), query)
                        || contains(c.getMobileNumber(), query)
                        || String.valueOf(c.getCustomerId()).contains(query))
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customer(id);
        boolean duplicate = customers.findAll().stream().anyMatch(existing ->
                !existing.getCustomerId().equals(id)
                        && (existing.getEmail().equalsIgnoreCase(request.getEmail())
                        || existing.getMobileNumber().equals(request.getMobileNumber())));
        if (duplicate) {
            throw new IllegalArgumentException("Email or mobile number already belongs to another customer");
        }

        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        CustomerOccupation occupationDetails = customer.getOccupationDetails();
        if (occupationDetails == null) {
            occupationDetails = new CustomerOccupation();
            occupationDetails.setCustomer(customer);
            customer.setOccupationDetails(occupationDetails);
        }
        occupationDetails.setOccupation(request.getOccupation());
        occupationDetails.setEmployerName(request.getEmployerName());
        occupationDetails.setEmploymentType(request.getEmploymentType());
        occupationDetails.setAnnualIncome(request.getAnnualIncome());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setEmail(request.getEmail());
        CustomerContact contact = customer.getContact();
        if (contact == null) {
            contact = new CustomerContact();
            contact.setCustomer(customer);
            customer.setContact(contact);
        }
        contact.setPreferredContactMode(request.getPreferredContactMode().name());

        customer = customers.save(customer);
        audit(id, "Customer", id, "UPDATE", "Customer details updated");
        return map(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customer(id);
        audit(id, "Customer", id, "DELETE", "Customer deleted");
        customers.delete(customer);
    }

    @Override
    public AddressResponse addAddress(Long customerId, AddressRequest request) {
        CustomerAddress address = new CustomerAddress();
        address.setCustomer(customer(customerId));
        fillAddress(address, request);
        address = addresses.save(address);
        audit(customerId, "Address", address.getAddressId(), "CREATE", "Address created");
        return map(address);
    }

    @Override
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        CustomerAddress address = address(id);
        fillAddress(address, request);
        address = addresses.save(address);
        audit(address.getCustomer().getCustomerId(), "Address", id, "UPDATE", "Address updated");
        return map(address);
    }

    @Override
    public void deleteAddress(Long id) {
        CustomerAddress address = address(id);
        Long customerId = address.getCustomer().getCustomerId();
        audit(customerId, "Address", id, "DELETE", "Address deleted");
        addresses.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesByCustomer(Long customerId) {
        customer(customerId);
        return addresses.findByCustomer_CustomerId(customerId).stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public NomineeResponse addNominee(Long customerId, NomineeRequest request) {
        Customer customer = customer(customerId);
        validateShare(customerId, null, request.getSharePercentage());

        Nominee nominee = new Nominee();
        nominee.setCustomer(customer);
        fillNominee(nominee, request);
        nominee = nominees.save(nominee);
        audit(customerId, "Nominee", nominee.getNomineeId(), "CREATE", "Nominee created");
        return map(nominee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NomineeResponse> getNomineesByCustomer(Long customerId) {
        customer(customerId);
        return nominees.findByCustomer_CustomerId(customerId).stream()
                .map(this::map).collect(Collectors.toList());
    }

    @Override
    public NomineeResponse updateNominee(Long id, NomineeRequest request) {
        Nominee nominee = nominee(id);
        Long customerId = nominee.getCustomer().getCustomerId();
        validateShare(customerId, id, request.getSharePercentage());
        fillNominee(nominee, request);
        nominee = nominees.save(nominee);
        audit(customerId, "Nominee", id, "UPDATE", "Nominee updated");
        return map(nominee);
    }

    @Override
    public void deleteNominee(Long id) {
        Nominee nominee = nominee(id);
        Long customerId = nominee.getCustomer().getCustomerId();
        audit(customerId, "Nominee", id, "DELETE", "Nominee deleted");
        nominees.delete(nominee);
    }

    private void validateShare(Long customerId, Long excludedNomineeId, BigDecimal share) {
        BigDecimal existingShare = nominees.findByCustomer_CustomerId(customerId).stream()
                .filter(item -> excludedNomineeId == null || !item.getNomineeId().equals(excludedNomineeId))
                .map(Nominee::getSharePercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (existingShare.add(share).compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException("Total nominee share percentage cannot exceed 100");
        }
    }

    @Override
    public DocumentResponse addDocument(Long customerId, String documentType, MultipartFile file) {
        Customer customer = customer(customerId);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a document to upload");
        }
        if (documentType == null || documentType.isBlank()) {
            throw new IllegalArgumentException("Document type must not be blank");
        }
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Document size must not exceed 5 MB");
        }

        String normalizedDocumentType = normalizeDocumentType(documentType);
        if (!ALLOWED_DOCUMENT_TYPES.contains(normalizedDocumentType)) {
            throw new IllegalArgumentException("Unsupported document type. Allowed types: " + ALLOWED_DOCUMENT_TYPES);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Only PDF, JPG, JPEG and PNG files are allowed");
        }

        try {
            Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(directory);

            String storedFilename = UUID.randomUUID() + extension;
            Path destination = directory.resolve(storedFilename).normalize();
            if (!destination.startsWith(directory)) {
                throw new IllegalArgumentException("Invalid document file path");
            }

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            CustomerDocument document = new CustomerDocument();
            document.setCustomer(customer);
            document.setDocumentType(normalizedDocumentType);
            document.setFileName(originalFilename == null ? storedFilename : originalFilename);
            document.setFilePath(destination.toString());
            document = documents.save(document);

            audit(customerId, "Document", document.getDocumentId(), "CREATE", "Document uploaded");
            return map(document);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save uploaded document", exception);
        }
    }

    private String normalizeDocumentType(String documentType) {
        String normalized = documentType.trim().toUpperCase(Locale.ROOT)
                .replace(" ", "_")
                .replace("-", "_");

        return switch (normalized) {
            case "AADHAR_CARD" -> "AADHAAR_CARD";
            case "DRIVINGLICENCE", "DRIVINGLICENCSE", "DRIVING_LICENCE" -> "DRIVING_LICENSE";
            default -> normalized;
        };
    }

    private String getExtension(String filename) {
        if (filename == null || filename.isBlank()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return filename.substring(dotIndex).toLowerCase(Locale.ROOT);
    }

    @Override
    public DocumentResponse updateDocument(Long id, String documentType, MultipartFile file) {
        CustomerDocument document = document(id);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a document to upload");
        }
        if (documentType == null || documentType.isBlank()) {
            throw new IllegalArgumentException("Document type must not be blank");
        }
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Document size must not exceed 5 MB");
        }

        String normalizedDocumentType = normalizeDocumentType(documentType);
        if (!ALLOWED_DOCUMENT_TYPES.contains(normalizedDocumentType)) {
            throw new IllegalArgumentException(
                    "Unsupported document type. Allowed types: " + ALLOWED_DOCUMENT_TYPES);
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Only PDF, JPG, JPEG and PNG files are allowed");
        }

        try {
            Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            String storedFilename = UUID.randomUUID() + extension;
            Path destination = directory.resolve(storedFilename).normalize();
            if (!destination.startsWith(directory)) {
                throw new IllegalArgumentException("Invalid document file path");
            }
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            document.setDocumentType(normalizedDocumentType);
            document.setFileName(file.getOriginalFilename() == null ? storedFilename : file.getOriginalFilename());
            document.setFilePath(destination.toString());
            document.setVerificationStatus("Pending");
            document = documents.save(document);
            Long customerId = document.getCustomer().getCustomerId();
            audit(customerId, "Document", id, "UPDATE", "Document file updated; verification reset to Pending");
            return map(document);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save updated document", exception);
        }
    }

    @Override
    public void deleteDocument(Long id) {
        CustomerDocument document = document(id);
        Long customerId = document.getCustomer().getCustomerId();
        audit(customerId, "Document", id, "DELETE", "Document deleted");
        documents.delete(document);
    }

    private void fillAddress(CustomerAddress address, AddressRequest request) {
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setAddressLine2(request.getAddressLine2());
        address.setAddressType(request.getAddressType());
    }

    private void fillNominee(Nominee nominee, NomineeRequest request) {
        nominee.setNomineeName(request.getNomineeName());
        nominee.setRelationship(request.getRelationship());
        nominee.setDateOfBirth(request.getDateOfBirth());
        nominee.setSharePercentage(request.getSharePercentage());
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    private CustomerResponse map(Customer customer) {
        CustomerResponse response = modelMapper.map(customer, CustomerResponse.class);
        if (customer.getContact() != null) {
            response.setPreferredContactMode(customer.getContact().getPreferredContactMode());
        }
        if (customer.getOccupationDetails() != null) {
            response.setOccupation(customer.getOccupationDetails().getOccupation().name());
            response.setEmployerName(customer.getOccupationDetails().getEmployerName());
            response.setEmploymentType(customer.getOccupationDetails().getEmploymentType().name());
            response.setAnnualIncome(customer.getOccupationDetails().getAnnualIncome());
        }
        return response;
    }

    private AddressResponse map(CustomerAddress address) {
        AddressResponse response = modelMapper.map(address, AddressResponse.class);
        response.setCustomerId(address.getCustomer().getCustomerId());
        return response;
    }

    private NomineeResponse map(Nominee nominee) {
        NomineeResponse response = modelMapper.map(nominee, NomineeResponse.class);
        response.setCustomerId(nominee.getCustomer().getCustomerId());
        return response;
    }

    private DocumentResponse map(CustomerDocument document) {
        DocumentResponse response = modelMapper.map(document, DocumentResponse.class);
        response.setCustomerId(document.getCustomer().getCustomerId());
        return response;
    }

}
