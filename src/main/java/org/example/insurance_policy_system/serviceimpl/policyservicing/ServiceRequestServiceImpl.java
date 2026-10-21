


import jakarta.persistence.EntityNotFoundException;

import org.example.insurance_policy_system.dto.policyservicing.ServiceRequestRequestDTO;
import org.example.insurance_policy_system.dto.policyservicing.ServiceRequestResponseDTO;
import org.example.insurance_policy_system.entity.customer.Customer;
import org.example.insurance_policy_system.entity.policy_insurance.Policy;
import org.example.insurance_policy_system.entity.policyservicing.ServiceRequest;
import org.example.insurance_policy_system.enums.policyservicing.ServiceRequestStatus;
import org.example.insurance_policy_system.repository.customer.CustomerRepository;
import org.example.insurance_policy_system.repository.policy_insurance.PolicyRepository;
import org.example.insurance_policy_system.repository.policyservicing.ServiceRequestRepository;
import org.example.insurance_policy_system.service.policyservicing.ServiceRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ServiceRequestServiceImpl
        implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public ServiceRequestServiceImpl(
            ServiceRequestRepository serviceRequestRepository,
            PolicyRepository policyRepository,
            CustomerRepository customerRepository,
            ModelMapper modelMapper) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    // 1. Create a service request
    @Override
    public ServiceRequestResponseDTO createRequest(
            ServiceRequestRequestDTO requestDTO) {

        Policy policy = policyRepository
                .findById(requestDTO.getPolicyId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Policy not found"));

        Customer customer = customerRepository
                .findById(requestDTO.getCustomerId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Customer not found"));

        // Convert request DTO to entity
        ServiceRequest request =
                modelMapper.map(requestDTO, ServiceRequest.class);

        // Set fields ModelMapper cannot determine from IDs
        request.setPolicy(policy);
        request.setCustomer(customer);
        request.setRequestNumber(generateRequestNumber());
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(ServiceRequestStatus.SUBMITTED);

        ServiceRequest savedRequest =
                serviceRequestRepository.save(request);

        return mapToResponseDTO(savedRequest);
    }

    // 2. Get all service requests
    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDTO> getAllRequests() {

        return serviceRequestRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // 3. Get a service request by ID
    @Override
    @Transactional(readOnly = true)
    public ServiceRequestResponseDTO getRequestById(
            Long requestId) {

        ServiceRequest request = serviceRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Service request not found: "
                                        + requestId));

        return mapToResponseDTO(request);
    }

    // 4. Update a service request
    @Override
    public ServiceRequestResponseDTO updateRequest(
            Long requestId,
            ServiceRequestRequestDTO requestDTO) {

        ServiceRequest request = serviceRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Service request not found: "
                                        + requestId));

        Policy policy = policyRepository
                .findById(requestDTO.getPolicyId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Policy not found"));

        Customer customer = customerRepository
                .findById(requestDTO.getCustomerId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Customer not found"));

        request.setPolicy(policy);
        request.setCustomer(customer);
        request.setRequestType(requestDTO.getRequestType());

        ServiceRequest updatedRequest =
                serviceRequestRepository.save(request);

        return mapToResponseDTO(updatedRequest);
    }

    // 5. Update request status
    @Override
    public ServiceRequestResponseDTO updateRequestStatus(
            Long requestId,
            ServiceRequestStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status is required");
        }

        ServiceRequest request = serviceRequestRepository
                .findById(requestId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Service request not found: "
                                        + requestId));

        request.setStatus(status);

        ServiceRequest updatedRequest =
                serviceRequestRepository.save(request);

        return mapToResponseDTO(updatedRequest);
    }

    // Generate a unique request number
    private String generateRequestNumber() {

        String requestNumber;

        do {
            requestNumber = "SRV-" +
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 20);
        } while (serviceRequestRepository
                .existsByRequestNumber(requestNumber));

        return requestNumber;
    }

    // Convert entity to response DTO using ModelMapper
    private ServiceRequestResponseDTO mapToResponseDTO(
            ServiceRequest request) {

        ServiceRequestResponseDTO response =
                modelMapper.map(
                        request,
                        ServiceRequestResponseDTO.class);

        // Map relationship IDs explicitly
        response.setPolicyId(request.getPolicy().getId());
        response.setCustomerId(request.getCustomer().getId());

        return response;
    }
}
