package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.Repository.CustomerRepository;
import com.thewinningteam.pms.Repository.ServiceProviderRepository;
import com.thewinningteam.pms.Repository.ServiceRepository;
import com.thewinningteam.pms.Service.CategoryService;
import com.thewinningteam.pms.Service.ServiceRequestService;
import com.thewinningteam.pms.exception.ServiceProviderNotFoundException;
import com.thewinningteam.pms.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {


    private ServiceRepository.ServiceRequestRepository serviceRequestRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final CategoryService categoryService;
    private final CustomerRepository customerRepository;

    @Override
    public void createServiceRequest(Long customerId, Long serviceProviderId, Long categoryId, String description, Address address) {
        // Retrieve the service provider by ID from the database
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new ServiceProviderNotFoundException("Service provider not found with ID: " + serviceProviderId));

        Customer existCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        Category existCagetory = categoryService.getCategoryById(categoryId)
                .orElseThrow(()-> new RuntimeException("Category not found with id: "+ categoryId));

        // Create and save the service request
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setCustomer(existCustomer);
        serviceRequest.setServiceProvider(serviceProvider);
        serviceRequest.setCategory(existCagetory);
        serviceRequest.setDescription(description);
        serviceRequest.setAddress(address);
        serviceRequest.setStatus(ServiceStatus.PENDING);
        serviceRequestRepository.save(serviceRequest);
    }


    @Override
    public List<ServiceRequest> getAllServiceRequests() {
        return serviceRequestRepository.findAll();
    }

    @Override
    public List<ServiceRequest> getServiceRequestsByCategory(Category category) {
        return serviceRequestRepository.findByCategory(category);
    }

    // Implement other methods as needed
}
