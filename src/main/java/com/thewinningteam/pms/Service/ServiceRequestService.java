package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.model.*;

import java.util.List;

public interface ServiceRequestService {
  void createServiceRequest(Long customerId, Long serviceProviderId, Long categoryId, String description, Address address);
    List<ServiceRequest> getAllServiceRequests();
    List<ServiceRequest> getServiceRequestsByCategory(Category category);
    // Add more methods as needed
}

