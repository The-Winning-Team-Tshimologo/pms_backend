package com.thewinningteam.pms.Controller;

import com.thewinningteam.pms.DTO.CreateServiceRequestDTO;
import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
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

import java.util.List;


@RequestMapping("service")
@RequiredArgsConstructor
@Tag(name = "Service Request")
@RestController
public class ServiceController {

    private final ServiceRequestServiceImpl requestService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/create/{serviceProviderId}/{categoryId}")
    public ResponseEntity<String> createServiceRequest(
            @RequestBody CreateServiceRequestDTO createServiceRequestDTO,
            Authentication authentication,
            @PathVariable Long serviceProviderId,
            @PathVariable Long categoryId
    )
    {
        try {
            // Call the service method to create the service request
            requestService.createServiceRequest(
                    new ServiceRequest(),
                    authentication,
                    serviceProviderId,
                    categoryId,
                    createServiceRequestDTO.getDescription(),
                    createServiceRequestDTO.getAddress()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Service request created successfully.");
        } catch (EntityNotFoundException | ServiceProviderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/create/{categoryId}")
    public ResponseEntity<?> createServiceRequest(
            @RequestBody CreateServiceRequestDTO createServiceRequestDTO,
            Authentication authentication,
            @PathVariable Long categoryId) {

        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            requestService.createServiceRequestSystemWide( new ServiceRequest(),
                    authentication,
                    categoryId,
                    createServiceRequestDTO.getDescription(),
                    createServiceRequestDTO.getAddress());

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

    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @GetMapping("/serviceRequests")
    public ResponseEntity<List<ServiceDTO>> getServiceRequestsWithCustomerByConnectedServiceProvider() {
        try {
            List<ServiceDTO> serviceDTOs = requestService.findServiceRequestsWithCustomerByConnectedServiceProvider();
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions appropriately, such as logging or returning an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @PostMapping("/accept/{serviceRequestId}")
    public ResponseEntity<?> acceptServiceRequest(@PathVariable Long serviceRequestId) {
        requestService.acceptServiceRequest(serviceRequestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @PostMapping("/decline/{serviceRequestId}")
    public ResponseEntity<?> declineServiceRequest(@PathVariable Long serviceRequestId) {
        requestService.declineServiceRequest(serviceRequestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    @PostMapping("/withdraw/{serviceRequestId}")
    public ResponseEntity<?> withdrawApplication(@PathVariable Long serviceRequestId) {
        requestService.withdrawApplication(serviceRequestId);
        return ResponseEntity.ok().build();
    }
}
