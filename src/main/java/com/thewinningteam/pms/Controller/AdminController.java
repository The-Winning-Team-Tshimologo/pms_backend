package com.thewinningteam.pms.Controller;


import com.thewinningteam.pms.Service.AdminService;
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
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

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

    @PostMapping("/toogle-service-providers-status")
    public ResponseEntity<String> getAllPendingServiceProviders(@PathVariable Long ServiceProviderID, @RequestParam boolean Accept ) {

        return ResponseEntity.ok("Admin get all Sp ");
    }

    @GetMapping("/pending-sp")
    public ResponseEntity<List<ServiceProvider>> getAllServiceProviders() {
        List<ServiceProvider> serviceProviders = adminService.findByAcceptanceStatus(AcceptanceStatus.PENDING);
        return ResponseEntity.ok(serviceProviders);
    }

}
