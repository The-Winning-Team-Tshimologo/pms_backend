package com.thewinningteam.pms.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewinningteam.pms.Repository.TokenRepository;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.Service.ServiceProviderService;
import com.thewinningteam.pms.emailservice.EmailService;
import com.thewinningteam.pms.emailservice.EmailTemplateName;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.Token;
import com.thewinningteam.pms.request.AuthenticationRequest;
import com.thewinningteam.pms.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final CustomerService customerService;
    private final AuthenticationService service;
    private final ObjectMapper objectMapper;
    private final ServiceProviderService serviceProviderService;
    private final AuthenticationService authservice;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> uploadDataAndImage(@RequestParam("data") String data,
                                                     @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Customer customer = objectMapper.readValue(data, Customer.class);


            if (image != null && !image.isEmpty()) {
                customer.setProfilePicture(image.getBytes());
            }

            Customer savedCustomer = customerService.SaveCustomer(customer);
            authservice.sendValidationEmail(customer);

            return new ResponseEntity<>("Customer created successfully.", HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid JSON format.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("A customer with the same email or username already exists.")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("An error occurred.", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred while processing the image.", HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public  void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }


}
