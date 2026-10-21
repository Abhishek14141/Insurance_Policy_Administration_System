package org.example.insurance_policy_system.service.policyservicing;




import org.example.insurance_policy_system.dto.policyservicing.ServiceRequestHistoryResponseDTO;

import java.util.List;

public interface ServiceRequestHistoryService {

    ServiceRequestHistoryResponseDTO recordHistory(
            Long requestId,
            String action,
            Long actionById,
            String remarks);

    List<ServiceRequestHistoryResponseDTO> getHistoryByRequestId(
            Long requestId);
}

