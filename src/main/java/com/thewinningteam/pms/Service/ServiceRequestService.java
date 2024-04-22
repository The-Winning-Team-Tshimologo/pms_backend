package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.AddressDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.model.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ServiceRequestService {
  void createServiceRequest(ServiceRequest request, Authentication connectedUser, Long serviceProviderId, Long categoryId, String description, Address address);
  List<ServiceDTO> findServiceRequestsWithCustomerByConnectedServiceProvider();
  // New methods to accept or decline service requests
  void acceptServiceRequest(Long serviceRequestId);
  void declineServiceRequest(Long serviceRequestId);

  void withdrawApplication(Long serviceRequestId);
  List<ServiceRequest> getAllServiceRequests();
  List<ServiceRequest> getServiceRequestsByCategory(Category category);
    // Add more methods as needed
}

