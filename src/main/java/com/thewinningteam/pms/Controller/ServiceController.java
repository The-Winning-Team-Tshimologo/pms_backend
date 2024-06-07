package com.thewinningteam.pms.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewinningteam.pms.DTO.*;
import com.thewinningteam.pms.Service.ServiceImpl.ServiceRequestServiceImpl;
import com.thewinningteam.pms.exception.AuthenticationException;
import com.thewinningteam.pms.exception.ServiceProviderNotFoundException;
import com.thewinningteam.pms.model.ServiceRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping("service")
@RequiredArgsConstructor
@Tag(name = "Service Request")
@RestController
public class ServiceController {

    private final ServiceRequestServiceImpl requestService;
    private final ObjectMapper objectMapper;


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/create/{serviceProviderId}/{categoryId}")
    public ResponseEntity<String> createServiceRequest(
            @RequestParam("requestDTOData") String requestDTOData,
            Authentication authentication,
            @PathVariable Long serviceProviderId,
            @PathVariable Long categoryId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    )
    {

        try {
            CreateServiceAndAppointmentDTO requestDTO = objectMapper.readValue(requestDTOData, CreateServiceAndAppointmentDTO.class);
            ServiceRequest serviceRequest = new ServiceRequest();

            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    System.out.println(file.getOriginalFilename());
                    serviceRequest.setPictures(file.getBytes());
                }
            }
            // Call the service method to create the service request
            requestService.createServiceRequest(
                    serviceRequest,
                    authentication,
                    serviceProviderId,
                    categoryId,
                    requestDTO.getCreateServiceRequestDTO().getDescription(),
                    requestDTO.getCreateServiceRequestDTO().getAddress(),
                    requestDTO.getCreateAppointmentDTO().getAppointmentDate(),
                    requestDTO.getCreateAppointmentDTO().getAppointmentMessage()

            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Service request created successfully.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid JSON format.");
        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred while processing the image.", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException | ServiceProviderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request.");
        }

    }

    @GetMapping("/customer")
    public ResponseEntity<List<CustomerServiceRequestedDTO>> getAllServiceRequestsForConnectedCustomer(Authentication authentication) {
        List<CustomerServiceRequestedDTO> serviceRequests = requestService.getAllServiceRequestsForConnectedCustomer(authentication);
        return ResponseEntity.ok(serviceRequests);
    }

    @PostMapping("/create/{categoryId}")
    public ResponseEntity<?> createServiceRequest(
            @RequestParam("requestDTOData") String requestDTOData,
            Authentication authentication,
            @PathVariable Long categoryId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            CreateServiceAndAppointmentDTO requestDTO = objectMapper.readValue(requestDTOData, CreateServiceAndAppointmentDTO.class);
            ServiceRequest serviceRequest = new ServiceRequest();
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    System.out.println(file.getOriginalFilename());
                    serviceRequest.setPictures(file.getBytes());
                }
            }


            requestService.createServiceRequestSystemWide(serviceRequest,
                    authentication,
                    categoryId,
                    requestDTO.getCreateServiceRequestDTO().getDescription(),
                    requestDTO.getCreateServiceRequestDTO().getAddress(),
                    requestDTO.getCreateAppointmentDTO().getAppointmentDate(),
                    requestDTO.getCreateAppointmentDTO().getAppointmentMessage());

            System.out.println("test 2 ");

            return ResponseEntity.ok("Service request created successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
    @GetMapping("/system-wide")
    public ResponseEntity<List<RequestSystemWideDTO>> getAllServiceRequestsSystemWide(Authentication authentication) {
        try {
            List<RequestSystemWideDTO> serviceRequests = requestService.findAllServiceRequestedSystemWide(authentication);
            return ResponseEntity.ok(serviceRequests);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // or HttpStatus.FORBIDDEN for access denied
        }
    }


    @GetMapping("/serviceRequests")
    public ResponseEntity<List<ServiceRequestWithAppointmentDTO>> getServiceRequestsWithCustomerByConnectedServiceProvider() {
        try {
            List<ServiceRequestWithAppointmentDTO> serviceDTOs = requestService.findServiceRequestsWithCustomerByConnectedServiceProvider2();
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions appropriately, such as logging or returning an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SERVICE_PROVIDER')")
    @GetMapping("/serviceRequests/{serviceRequestId}")
    public ResponseEntity<ServiceRequestWithAppointmentDTO> getServiceRequestsWithAppointmentById(@PathVariable Long serviceRequestId) {
        try {
            ServiceRequestWithAppointmentDTO serviceDTOs = requestService.findServiceRequestsWithAppointmentById(serviceRequestId);
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @PostMapping("/accept/{serviceRequestId}")
    public ResponseEntity<?> acceptServiceRequest(@PathVariable Long serviceRequestId) throws JsonProcessingException {
        requestService.acceptServiceRequest(serviceRequestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @PostMapping("/decline/{serviceRequestId}")
    public ResponseEntity<?> declineServiceRequest(@PathVariable Long serviceRequestId) throws JsonProcessingException {
        requestService.declineServiceRequest(serviceRequestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @PostMapping("/withdraw/{serviceRequestId}")
    public ResponseEntity<?> withdrawApplication(@PathVariable Long serviceRequestId) throws JsonProcessingException {
        requestService.withdrawApplication(serviceRequestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-completed/{serviceRequestId}")
    public ResponseEntity<String> confirmCompletedProject(@PathVariable Long serviceRequestId, Authentication authentication) {
        try {
            // Call the service method to confirm the completion of the project
            requestService.confirmCompletedProject(serviceRequestId, authentication);

            return ResponseEntity.ok("Project completion confirmed successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to confirm project completion: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllServices(){
        try{
            Long totalServices = requestService.getTotalServiceRequests();
          return new ResponseEntity<>(totalServices, HttpStatus.OK);
        }catch (Exception e)
        {
            return (ResponseEntity<Long>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/assign/{serviceId}/{customerId}")
    public ResponseEntity<String> assignServiceProviderToCustomerService(
            @PathVariable Long serviceId,
            @PathVariable Long customerId) {
        try {
            requestService.assignServiceProviderToCustomerService(serviceId, customerId);
            return ResponseEntity.ok("Service provider assigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign service provider: " + e.getMessage());
        }
    }

}
