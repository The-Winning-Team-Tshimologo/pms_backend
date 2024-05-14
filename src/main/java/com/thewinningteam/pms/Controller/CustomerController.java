package com.thewinningteam.pms.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.DTO.CustomerServiceDTO;
import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.Service.AdminService;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.auth.AuthenticationService;
import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
//@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("customer")
public class CustomerController {

    private final CustomerService customerService;
    private final AdminService adminService;


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/put/{customerId}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok("test"+ customerId);
    }




    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SERVICE_PROVIDER')")
    @GetMapping("get-customer/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        Optional<CustomerDTO> customerDTOOptional = customerService.GetCustomerById(customerId);
        return customerDTOOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER','ROLE_ADMIN')")
    @GetMapping("/browse-sp")
    public ResponseEntity<List<ServiceProviderDTO>> getAllAcceptedServiceProviders() {
        List<ServiceProviderDTO> serviceProviders = adminService.findByAcceptanceStatus(AcceptanceStatus.ACCEPTED);
        return ResponseEntity.ok(serviceProviders);
    }
}
