package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.AddressDTO;
import com.thewinningteam.pms.DTO.CustomerServiceRequestedDTO;
import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public interface ServiceRequestService {
  void createServiceRequest(
          ServiceRequest request,
          Authentication connectedUser,
          Long serviceProviderId,
          Long categoryId,
          String description,
          Address address,
          Timestamp appointmentDate,
          String appointmentMessage);
  List<ServiceDTO> findServiceRequestsWithCustomerByConnectedServiceProvider();
  // New methods to accept or decline service requests
  void acceptServiceRequest(Long serviceRequestId);
  void declineServiceRequest(Long serviceRequestId);

  void withdrawApplication(Long serviceRequestId);
  List<ServiceRequest> getAllServiceRequests();
  List<ServiceRequest> getServiceRequestsByCategory(Category category);

  void createServiceRequestSystemWide(
          ServiceRequest request,
          Authentication connectedUser,
          Long categoryId,
          String description,
          Address address,
          Timestamp appointmentDate,
          String appointmentMessage);
  List<CustomerServiceRequestedDTO> getAllServiceRequestsForConnectedCustomer(Authentication connectedUser);
  List<RequestSystemWideDTO> findAllServiceRequestedSystemWide(Authentication authentication);

  void confirmCompletedProject(Long serviceRequestId, Authentication authentication);

  long getTotalServiceRequests();
  void assignServiceProviderToCustomerService(Long serviceId, Long customerId);
    // Add more methods as needed
}

