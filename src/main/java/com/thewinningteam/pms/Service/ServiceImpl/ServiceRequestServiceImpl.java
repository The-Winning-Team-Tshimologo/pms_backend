package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.CustomerServiceRequestedDTO;
import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.Repository.AppointmentRepository;
import com.thewinningteam.pms.Repository.CustomerRepository;
import com.thewinningteam.pms.Repository.ServiceProviderRepository;
import com.thewinningteam.pms.Repository.ServiceRepository;
import com.thewinningteam.pms.Service.CategoryService;
import com.thewinningteam.pms.Service.ServiceRequestService;
import com.thewinningteam.pms.exception.ServiceProviderNotFoundException;
import com.thewinningteam.pms.model.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


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
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void createServiceRequest(
            ServiceRequest request,
            Authentication connectedUser,
            Long serviceProviderId,
            Long categoryId,
            String description,
            Address address,
            Timestamp appointmentDate,
            String appointmentMessage)
    {
        // Retrieve the logged-in customer from the Authentication object
        Customer customer = (Customer) connectedUser.getPrincipal();

        // Set the logged-in customer to the service request
        request.setCustomer(customer);
        request.setDescription(description);

        // Set the service status to PENDING (or any default status)
        request.setStatus(ServiceStatus.PENDING);

        // Set the address to the service request
        request.setAddress(address);

        // Retrieve the service provider by ID
        Optional<ServiceProvider> optionalServiceProvider =
                serviceProviderRepository.findById(serviceProviderId);

        // Check if the service provider exists
        if (optionalServiceProvider.isPresent()) {
            ServiceProvider serviceProvider = optionalServiceProvider.get();
            // Set the retrieved service provider to the request
            request.setServiceProvider(serviceProvider);

            // Retrieve the category by ID
            Optional<Category> optionalCategory = categoryService.getCategoryById(categoryId);

            // Check if the category exists
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                // Set the retrieved category to the request
                request.setCategory(category);
            } else {
                // If the category with the given ID does not exist, throw an exception or handle the case accordingly
                throw new EntityNotFoundException("Category with ID " + categoryId + " not found.");
            }

            // Save the service request
            serviceRepository.save(request);
        } else {
            // If the service provider with the given ID does not exist, throw an exception or handle the case accordingly
            throw new ServiceProviderNotFoundException("Service provider with ID " + serviceProviderId + " not found.");
        }

        Appointment appointment = new Appointment();
        appointment.setRequestDate(new Timestamp(System.currentTimeMillis()));
        appointment.setAppointmentDate(new Date(appointmentDate.getTime())); // Assuming appointmentDate is of type java.util.Date
        appointment.setMessage(appointmentMessage);
        appointment.setCustomer(customer);
        appointment.setService(request);


        // Save the appointment
        appointmentRepository.save(appointment);

    }

    @Override
    public void createServiceRequestSystemWide(
            ServiceRequest request,
            Authentication connectedUser,
            Long categoryId,
            String description,
            Address address,
            Timestamp appointmentDate,
            String appointmentMessage) {

        // Retrieve the logged-in customer from the Authentication object
        Customer customer = (Customer) connectedUser.getPrincipal();

        // Set the logged-in customer to the service request
        request.setCustomer(customer);
        request.setDescription(description);

        // Set the service status to PENDING (or any default status)
        request.setStatus(ServiceStatus.PENDING);

        // Set the address to the service request
        request.setAddress(address);

        // Retrieve the category by ID
        Optional<Category> optionalCategory = categoryService.getCategoryById(categoryId);

        // Check if the category exists
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            // Set the retrieved category to the request
            request.setCategory(category);
        } else {
            // If the category with the given ID does not exist, throw an exception or handle the case accordingly
            throw new EntityNotFoundException("Category with ID " + categoryId + " not found.");
        }

        // Save the service request
        serviceRepository.save(request);

        // Create the appointment
        Appointment appointment = new Appointment();
        appointment.setRequestDate(new Timestamp(System.currentTimeMillis()));
        appointment.setAppointmentDate(new Date(appointmentDate.getTime())); // Assuming appointmentDate is of type java.util.Date
        appointment.setMessage(appointmentMessage);
        appointment.setCustomer(customer);
        appointment.setService(request);


        // Save the appointment
        appointmentRepository.save(appointment);
    }




    @Override
    public List<ServiceDTO> findServiceRequestsWithCustomerByConnectedServiceProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SERVICE_PROVIDER"))) {
            throw new AccessDeniedException("User is not a service provider");
        }

        ServiceProvider serviceProvider = (ServiceProvider) authentication.getPrincipal();
        Long serviceProviderId = serviceProvider.getUserId();

        // Call the repository method to get ServiceDTO objects directly
        return serviceRepository.findServiceRequestsWithCustomerByServiceProviderId(serviceProviderId);
    }

    // Method to convert ServiceRequest entities to ServiceDTO objects
    private ServiceDTO toDTO(ServiceRequest serviceRequest) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceId(serviceRequest.getServiceId());
        dto.setPictures(serviceRequest.getPictures());
        dto.setAddress(serviceRequest.getAddress());
        dto.setDescription(serviceRequest.getDescription());
        dto.setStatus(serviceRequest.getStatus());
        dto.setCustomer(serviceRequest.getCustomer());
        dto.setCategory(serviceRequest.getCategory());
        return dto;
    }

    // Method to convert a list of ServiceRequest entities to a list of ServiceDTO objects
    private List<ServiceDTO> toDTO(List<ServiceRequest> serviceRequests) {
        return serviceRequests.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestSystemWideDTO> findAllServiceRequestedSystemWide(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SERVICE_PROVIDER"))) {
            throw new AccessDeniedException("User is not a service provider");
        }

        return serviceRepository.findAllWithoutServiceProvider();
    }

    @Override
    public void confirmCompletedProject(Long serviceRequestId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        // Check if the authentication principal is a connected service provider
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SERVICE_PROVIDER"))) {
            throw new AccessDeniedException("User is not a connected service provider");
        }

        // Get the connected service provider
        ServiceProvider serviceProvider = (ServiceProvider) userDetails;

        // Retrieve the service request by its ID
        Optional<ServiceRequest> optionalServiceRequest = serviceRepository.findById(serviceRequestId);
        if (optionalServiceRequest.isEmpty()) {
            throw new IllegalArgumentException("Service request not found");
        }
        ServiceRequest serviceRequest = optionalServiceRequest.get();

//        // Check if the service request belongs to the connected service provider
//        if (!serviceRequest.getServiceProvider().equals(serviceProvider)) {
//            throw new AccessDeniedException("Service request does not belong to the connected service provider");
//        }

        // Confirm the completion of the project
        serviceRequest.setCompleted(true);
        serviceRepository.save(serviceRequest);
    }

    @Override
    public long getTotalServiceRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AccessDeniedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        if(!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            throw new AccessDeniedException("User is not a Admin");
        }

        return  serviceRepository.countAllServiceRequests();
    }

    @Override
    public void acceptServiceRequest(Long serviceRequestId) {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve the service provider from the authentication principal
            ServiceProvider serviceProvider = (ServiceProvider) authentication.getPrincipal();
            Long serviceProviderId = serviceProvider.getUserId();

            // Retrieve the service request by its ID
            Optional<ServiceRequest> optionalServiceRequest = serviceRepository.findById(serviceRequestId);

            // Check if the service request exists
            if (optionalServiceRequest.isPresent()) {
                ServiceRequest serviceRequest = optionalServiceRequest.get();

                // Check if the connected service provider matches the service provider of the service request
                if (!Objects.equals(serviceProviderId, serviceRequest.getServiceProvider().getUserId())) {
                    throw new IllegalArgumentException("Service request does not belong to the connected service provider");
                }

                // Retrieve the customer associated with the service request
                Customer customer = serviceRequest.getCustomer();

                // Update the status of the service request to indicate acceptance
                serviceRequest.setStatus(ServiceStatus.ACCEPTED);

                // Save the updated service request
                serviceRepository.save(serviceRequest);
            } else {
                // Handle the case where the service request with the given ID is not found
                throw new IllegalArgumentException("Service request with ID " + serviceRequestId + " not found");
            }
        } else {
            // Handle the case where the user is not authenticated
            throw new RuntimeException("User not authenticated");
        }
    }

    @Override
    public void declineServiceRequest(Long serviceRequestId) {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve the service provider from the authentication principal
            ServiceProvider serviceProvider = (ServiceProvider) authentication.getPrincipal();
            Long serviceProviderId = serviceProvider.getUserId();

            // Retrieve the service request by its ID
            Optional<ServiceRequest> optionalServiceRequest = serviceRepository.findById(serviceRequestId);

            // Check if the service request exists
            if (optionalServiceRequest.isPresent()) {
                ServiceRequest serviceRequest = optionalServiceRequest.get();

                // Check if the connected service provider matches the service provider of the service request
                if (!Objects.equals(serviceProviderId, serviceRequest.getServiceProvider().getUserId())) {
                    throw new IllegalArgumentException("Service request does not belong to the connected service provider");
                }

                // Retrieve the customer associated with the service request
                Customer customer = serviceRequest.getCustomer();

                // Update the status of the service request to indicate decline
                serviceRequest.setStatus(ServiceStatus.REJECTED);

                // Save the updated service request
                serviceRepository.save(serviceRequest);
            } else {
                // Handle the case where the service request with the given ID is not found
                throw new IllegalArgumentException("Service request with ID " + serviceRequestId + " not found");
            }
        } else {
            // Handle the case where the user is not authenticated
            throw new RuntimeException("User not authenticated");
        }
    }
    @Override
    public void withdrawApplication(Long serviceRequestId) {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve the service provider from the authentication principal
            ServiceProvider serviceProvider = (ServiceProvider) authentication.getPrincipal();
            Long serviceProviderId = serviceProvider.getUserId();

            // Retrieve the service request by its ID
            Optional<ServiceRequest> optionalServiceRequest = serviceRepository.findById(serviceRequestId);

            // Check if the service request exists
            if (optionalServiceRequest.isPresent()) {
                ServiceRequest serviceRequest = optionalServiceRequest.get();

                // Check if the connected service provider matches the service provider of the service request
                if (!Objects.equals(serviceProviderId, serviceRequest.getServiceProvider().getUserId())) {
                    throw new IllegalArgumentException("Service request does not belong to the connected service provider");
                }

                // Check if the service request is in a state where the application can be withdrawn
                if (serviceRequest.getStatus() != ServiceStatus.ACCEPTED) {
                    throw new IllegalStateException("Cannot withdraw application for a service request that is not accepted");
                }

                // Set the service provider of the service request to null to withdraw the application
                serviceRequest.setServiceProvider(null);

                // Retrieve the customer associated with the service request
                Customer customer = serviceRequest.getCustomer();

                // Update the status of the service request to indicate withdrawal
                serviceRequest.setStatus(ServiceStatus.REJECTED);

                // Save the updated service request
                serviceRepository.save(serviceRequest);
            } else {
                // Handle the case where the service request with the given ID is not found
                throw new IllegalArgumentException("Service request with ID " + serviceRequestId + " not found");
            }
        } else {
            // Handle the case where the user is not authenticated
            throw new RuntimeException("User not authenticated");
        }
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


}
