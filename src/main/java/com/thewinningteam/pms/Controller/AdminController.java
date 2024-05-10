package com.thewinningteam.pms.Controller;


import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.Service.AdminService;
import com.thewinningteam.pms.Service.ServiceImpl.ServiceProviderServiceImpl;
import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.ServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
//@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;
    private final ServiceProviderServiceImpl serviceProviderServiceImpl;

//    @GetMapping("/pending-service-providers")
//    public ResponseEntity<List<ServiceProvider>> getAllPendingServiceProviders() {
//        List<ServiceProvider> pendingServiceProviders = adminService.getAllPendingServiceProviders();
//        if (pendingServiceProviders.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(pendingServiceProviders, HttpStatus.OK);
//    }

    @GetMapping("/pending-service-providers")
    public ResponseEntity<String> getAllPendingServiceProviders() {
//        List<ServiceProvider> pendingServiceProviders = adminService.getAllPendingServiceProviders();
//        if (pendingServiceProviders.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
        return ResponseEntity.ok("Admin get all Sp ");
    }

    @PostMapping("/toogle-service-providers-status/{ServiceProviderID}")
    public ResponseEntity<String> getAllPendingServiceProviders(@PathVariable Long ServiceProviderID, @RequestParam boolean Accept ) {
        ServiceProviderDTO serviceProviders;
        AcceptanceStatus acceptanceStatus;
        if (Accept){
            acceptanceStatus = AcceptanceStatus.ACCEPTED;
        }else {
            acceptanceStatus = AcceptanceStatus.REJECTED;
        }

        serviceProviders = adminService.ChangeAcceptanceStatus(ServiceProviderID, acceptanceStatus);
        return ResponseEntity.ok(serviceProviders.getFirstName() + " has been " + acceptanceStatus.name());
    }

    @GetMapping("/pending-sp")
    public ResponseEntity<List<ServiceProviderDTO>> getAllServiceProviders() {
        List<ServiceProviderDTO> serviceProviders = adminService.findByAcceptanceStatus(AcceptanceStatus.PENDING);
        return ResponseEntity.ok(serviceProviders);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    @GetMapping("/get-sp/{spId}")
    public ResponseEntity<ServiceProviderDTO> getServiceProvider(@PathVariable Long spId){
        return ResponseEntity.ok(serviceProviderServiceImpl.GetServiceProviderById(spId));
    }



}
