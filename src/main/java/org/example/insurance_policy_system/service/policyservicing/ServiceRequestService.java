package org.example.insurance_policy_system.service.policyservicing;




import org.example.insurance_policy_system.dto.policyservicing.ServiceRequestRequestDTO;
import org.example.insurance_policy_system.dto.policyservicing.ServiceRequestResponseDTO;
import org.example.insurance_policy_system.enums.policyservicing.ServiceRequestStatus;

import java.util.List;

public interface ServiceRequestService {

    ServiceRequestResponseDTO createRequest(
            ServiceRequestRequestDTO requestDTO);

    List<ServiceRequestResponseDTO> getAllRequests();

    ServiceRequestResponseDTO getRequestById(Long requestId);

    ServiceRequestResponseDTO updateRequest(
            Long requestId,
            ServiceRequestRequestDTO requestDTO);

    ServiceRequestResponseDTO updateRequestStatus(
            Long requestId,
            ServiceRequestStatus status);
}

