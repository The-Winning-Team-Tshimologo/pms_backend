package com.thewinningteam.pms.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thewinningteam.pms.DTO.*;
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
          String appointmentMessage) throws JsonProcessingException;
  List<ServiceDTO> findServiceRequestsWithCustomerByConnectedServiceProvider();
  // New methods to accept or decline service requests
  void acceptServiceRequest(Long serviceRequestId) throws JsonProcessingException;
  void declineServiceRequest(Long serviceRequestId) throws JsonProcessingException;

  void withdrawApplication(Long serviceRequestId) throws JsonProcessingException;
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

  List<ServiceRequestWithAppointmentDTO> findServiceRequestsWithCustomerByConnectedServiceProvider2();

  ServiceRequestWithAppointmentDTO findServiceRequestsWithAppointmentById(Long serviceId);

  List<RequestSystemWideDTO> findAllServiceRequestedSystemWide(Authentication authentication);

  void confirmCompletedProject(Long serviceRequestId, Authentication authentication);

  long getTotalServiceRequests();
  void assignServiceProviderToCustomerService(Long serviceId, Long customerId);
    // Add more methods as needed
}

