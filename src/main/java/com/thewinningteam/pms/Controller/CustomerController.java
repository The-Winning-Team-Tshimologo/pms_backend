package com.thewinningteam.pms.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.DTO.CustomerServiceDTO;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.auth.AuthenticationService;
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
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/{customerId}")
    public ResponseEntity<String> updateCustomer2(
            @PathVariable Long customerId,
            @RequestParam("data") String data,
            @RequestParam(value = "image", required = false) MultipartFile image
    )
    {

        try {
            Customer updatedCustomer = objectMapper.readValue(data, Customer.class);

            if (image != null && !image.isEmpty()) {
                updatedCustomer.setProfilePicture(image.getBytes());
            }

            updatedCustomer.setUserId(customerId);
            Optional<CustomerDTO> optionalCustomer = customerService.updateCustomer(updatedCustomer);
            return optionalCustomer
                    .map(customer -> new ResponseEntity<>("User updated successfully", HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>("User not found with id: " + customerId, HttpStatus.NOT_FOUND));

        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid JSON format.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.BAD_REQUEST);

        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred while processing the image.", HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/put/{customerId}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok("test"+ customerId);
    }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/logissue/{customerId}/{spId}")
    public ResponseEntity<String> updateCustomerd(@PathVariable Long customerId,
                                                  @PathVariable(required = false) Long spId,@RequestParam("data") String data){
        return ResponseEntity.ok("test"+ spId);
    }



    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("get-customer/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        Optional<CustomerDTO> customerDTOOptional = customerService.GetCustomerById(customerId);
        return customerDTOOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
