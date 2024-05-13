package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.CustomerServiceRequestedDTO;
import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.Repository.AppointmentRepository;
import com.thewinningteam.pms.Repository.ServiceProviderRepository;
import com.thewinningteam.pms.Repository.ServiceRepository;
import com.thewinningteam.pms.Service.CategoryService;
import com.thewinningteam.pms.Service.ServiceRequestService;
import com.thewinningteam.pms.exception.ServiceProviderNotFoundException;
import com.thewinningteam.pms.mapper.ServiceMapper;
import com.thewinningteam.pms.model.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {


    private final ServiceRepository serviceRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final CategoryService categoryService;
    private final AppointmentRepository appointmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestServiceImpl.class);


    // Method to create a service request with a connected service provider
    @Override
    public void createServiceRequest(
            ServiceRequest request,
            Authentication connectedUser,
            Long serviceProviderId,
            Long categoryId,
            String description,
            Address address,
            Timestamp appointmentDate,
            String appointmentMessage

    ) {
        Customer customer = getLoggedInCustomer(connectedUser);

        request.setCustomer(customer);
        request.setDescription(description);
        request.setStatus(ServiceStatus.PENDING);
        request.setAddress(address);

        ServiceProvider serviceProvider = getServiceProvider(serviceProviderId);
        Category category = getCategory(categoryId);
        request.setServiceProvider(serviceProvider);
        request.setCategory(category);

        serviceRepository.save(request);

        saveAppointment(customer, request, appointmentDate, appointmentMessage);
    }

    // Method to create a service request system-wide (without a connected service provider)
    @Override
    public void createServiceRequestSystemWide(
            ServiceRequest request,
            Authentication connectedUser,
            Long categoryId,
            String description,
            Address address,
            Timestamp appointmentDate,
            String appointmentMessage

    ) {
        Customer customer = getLoggedInCustomer(connectedUser);

        request.setCustomer(customer);
        request.setDescription(description);
        request.setStatus(ServiceStatus.PENDING);
        request.setAddress(address);

        Category category = getCategory(categoryId);
        request.setCategory(category);

        serviceRepository.save(request);

        saveAppointment(customer, request, appointmentDate, appointmentMessage);
    }

    // Method to find service requests with customer information by connected service provider
    @Override
    public List<ServiceDTO> findServiceRequestsWithCustomerByConnectedServiceProvider() {
        Authentication authentication = getAuthentication();
        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);

        return serviceRepository.findServiceRequestsWithCustomerByServiceProviderId(serviceProvider.getUserId())
                .stream()
                .map(ServiceMapper::toDTO) // Using lambda expression
                .collect(Collectors.toList());
    }


    // Method to find all service requests system-wide
    @Override
    public List<RequestSystemWideDTO> findAllServiceRequestedSystemWide(Authentication authentication) {
        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);

        return serviceRepository.findAllWithoutServiceProvider();
    }


    // Helper method to retrieve the logged-in customer from authentication
    private Customer getLoggedInCustomer(Authentication connectedUser) {
        return (Customer) connectedUser.getPrincipal();
    }

    // Helper method to retrieve a service provider by ID
    private ServiceProvider getServiceProvider(Long serviceProviderId) {
        return serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new ServiceProviderNotFoundException("Service provider with ID " + serviceProviderId + " not found."));
    }

    // Helper method to retrieve a category by ID
    private Category getCategory(Long categoryId) {
        return categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found."));
    }

    // Helper method to save an appointment associated with a service request
    private void saveAppointment(Customer customer, ServiceRequest request, Timestamp appointmentDate, String appointmentMessage) {
        Appointment appointment = new Appointment();
        appointment.setRequestDate(new Timestamp(System.currentTimeMillis()));
        appointment.setAppointmentDate(new Date(appointmentDate.getTime()));
        appointment.setMessage(appointmentMessage);
        appointment.setCustomer(customer);
        appointment.setService(request);

        appointmentRepository.save(appointment);
    }


    // Helper method to retrieve the authentication object
    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication;
    }

    // Helper method to retrieve the service provider from authentication
    private ServiceProvider getServiceProviderFromAuthentication(Authentication authentication) {
        if (!isUserServiceProvider(authentication)) {
            throw new AccessDeniedException("User is not a service provider");
        }
        return (ServiceProvider) authentication.getPrincipal();
    }

    // Helper method to check if the user is a service provider
    private boolean isUserServiceProvider(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SERVICE_PROVIDER"));
    }


    // Method to confirm the completion of a project
    @Override
    @Transactional
    public void confirmCompletedProject(Long serviceRequestId, Authentication authentication) {
        validateAuthentication(authentication);

        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);

        ServiceRequest serviceRequest = getServiceRequestById(serviceRequestId);
        serviceRequest.setCompleted(true);
        serviceRepository.save(serviceRequest);
    }

    // Method to get the total number of service requests
    @Override
    public long getTotalServiceRequests() {
        validateAdminAuthentication();

        return serviceRepository.countAllServiceRequests();
    }

    // Method to accept a service request
    @Override
    @Transactional
    public void acceptServiceRequest(Long serviceRequestId) {
        Authentication authentication = getAuthentication();
        validateAuthentication(authentication);

        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);
        ServiceRequest serviceRequest = validateAndRetrieveServiceRequest(serviceRequestId, serviceProvider);

        serviceRequest.setStatus(ServiceStatus.ACCEPTED);
        serviceRepository.save(serviceRequest);
    }

    // Method to decline a service request
    @Override
    @Transactional
    public void declineServiceRequest(Long serviceRequestId) {
        Authentication authentication = getAuthentication();
        validateAuthentication(authentication);

        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);
        ServiceRequest serviceRequest = validateAndRetrieveServiceRequest(serviceRequestId, serviceProvider);

        serviceRequest.setStatus(ServiceStatus.REJECTED);
        serviceRepository.save(serviceRequest);
    }

    // Method to withdraw an application for a service request
    @Override
    @Transactional
    public void withdrawApplication(Long serviceRequestId) {
        Authentication authentication = getAuthentication();
        validateAuthentication(authentication);

        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);
        ServiceRequest serviceRequest = validateAndRetrieveServiceRequest(serviceRequestId, serviceProvider);

        if (serviceRequest.getStatus() != ServiceStatus.ACCEPTED) {
            throw new IllegalStateException("Cannot withdraw application for a service request that is not accepted");
        }

        serviceRequest.setServiceProvider(null);
        serviceRequest.setStatus(ServiceStatus.REJECTED);
        serviceRepository.save(serviceRequest);
    }

    // Helper method to validate authentication
    private void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
    }

    // Helper method to validate admin authentication
    private void validateAdminAuthentication() {
        Authentication authentication = getAuthentication();
        validateAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("User is not an admin");
        }
    }

    // Helper method to retrieve a service request by ID
    private ServiceRequest getServiceRequestById(Long serviceRequestId) {
        return serviceRepository.findById(serviceRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Service request not found"));
    }

    // Helper method to validate and retrieve a service request by ID and service provider
    private ServiceRequest validateAndRetrieveServiceRequest(Long serviceRequestId, ServiceProvider serviceProvider) {
        ServiceRequest serviceRequest = getServiceRequestById(serviceRequestId);
        if (!serviceRequest.getServiceProvider().equals(serviceProvider)) {
            throw new IllegalArgumentException("Service request does not belong to the connected service provider");
        }
        return serviceRequest;
    }



    @Override
    public List<CustomerServiceRequestedDTO> getAllServiceRequestsForConnectedCustomer(Authentication connectedUser) {
        // Retrieve the logged-in customer from the Authentication object
        Customer customer = (Customer) connectedUser.getPrincipal();

        // Query the database for service requests associated with the customer
        return serviceRepository.getCustomerServiceRequestedDTOByCustomerId(customer.getUserId());
    }


    @Override
    public List<ServiceRequest> getAllServiceRequests() {
        return serviceRepository.findAll();
    }

    @Override
    public List<ServiceRequest> getServiceRequestsByCategory(Category category) {
        return serviceRepository.findByCategory(category);
    }

    // Method to assign a service provider to a customer service
    @Transactional
    @Override
    public void assignServiceProviderToCustomerService(Long serviceId, Long customerId) {
        Authentication authentication = getAuthentication();
        ServiceProvider serviceProvider = getServiceProviderFromAuthentication(authentication);

        validateUserRole(authentication);
        checkServiceProviderAssignment(serviceId, serviceProvider);

        try {
            serviceRepository.assignServiceProviderToCustomerService(serviceId, serviceProvider.getUserId(), customerId);
        } catch (Exception e) {
            handleAssignmentFailure(e);
        }
    }

    private void validateUserRole(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SERVICE_PROVIDER"))) {
            throw new AccessDeniedException("User is not a service provider");
        }
    }

    private void checkServiceProviderAssignment(Long serviceId, ServiceProvider serviceProvider) {
        ServiceRequest existingAssignment = serviceRepository.findByServiceIdAndServiceProviderUserId(serviceId, serviceProvider.getUserId());
        if (existingAssignment != null) {
            throw new RuntimeException("Service provider is already assigned to this service");
        }
    }

    private void handleAssignmentFailure(Exception e) {
        logger.error("Failed to assign service provider to customer service", e);
        throw new RuntimeException("Failed to assign service provider to customer service: " + e.getMessage());
    }




}
