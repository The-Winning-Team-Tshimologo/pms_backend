package com.thewinningteam.pms.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.Repository.TokenRepository;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.Service.ServiceProviderService;
import com.thewinningteam.pms.emailservice.EmailService;
import com.thewinningteam.pms.emailservice.EmailTemplateName;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.Profile;
import com.thewinningteam.pms.model.ServiceProvider;
import com.thewinningteam.pms.model.Token;
import com.thewinningteam.pms.request.AuthenticationRequest;
import com.thewinningteam.pms.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
@SecurityRequirement(name = "Authorization")
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class AuthenticationController {

    private final CustomerService customerService;
    private final AuthenticationService service;
    private final ObjectMapper objectMapper;
    private final ServiceProviderService serviceProviderService;
    private final AuthenticationService authservice;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUpCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            customerService.signUpCustomer(customerDTO);
            return ResponseEntity.ok("Customer signed up successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sign up customer: " + e.getMessage());
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


    @PostMapping("/signup-sp")
    public ResponseEntity<Map<String, Object>> registerServiceProvider(
            @RequestParam("data") String data,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "identityDocument", required = false) MultipartFile identityDocument,
            @RequestParam(value = "qualification", required = false) MultipartFile qualification,
            @RequestParam(value = "criminalRecord", required = false) MultipartFile criminalRecord,
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            @RequestParam(value = "bankStatement", required = false) MultipartFile bankStatement,
            @RequestParam("profile") String profile) {

        Map<String, Object> response = new HashMap<>();
        try {

            ServiceProvider serviceProvider = parseServiceProvider(data, profile);
            handleFiles(serviceProvider, profilePicture, identityDocument, qualification, criminalRecord, resume, bankStatement);

            ServiceProvider savedCustomer = serviceProviderService.SaveServiceProvider(serviceProvider);
            response.put("message", "Service Provider created successfully");
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e) {
            response.put("message", "Invalid JSON format");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IllegalArgumentException | IOException e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    private ServiceProvider parseServiceProvider(String data, String profile) throws JsonProcessingException {
        ServiceProvider serviceProvider = objectMapper.readValue(data, ServiceProvider.class);
        Profile spProfile = objectMapper.readValue(profile, Profile.class);
        serviceProvider.setEnabled(true);
        serviceProvider.setProfile(spProfile);
        return serviceProvider;
    }

    private void handleFiles(ServiceProvider serviceProvider, MultipartFile... files) throws IOException {
        if (files[0] != null && !files[0].isEmpty()) {
            serviceProvider.setProfilePicture(files[0].getBytes());
        }
        if (files[1] != null && !files[1].isEmpty()) {
            serviceProvider.setIdentityDocument(files[1].getBytes());
        }
        if (files[2] != null && !files[2].isEmpty()) {
            serviceProvider.setQualification(files[2].getBytes());
        }
        if (files[3] != null && !files[3].isEmpty()) {
            serviceProvider.setCriminalRecord(files[3].getBytes());
        }
        if (files[4] != null && !files[4].isEmpty()) {
            serviceProvider.setResume(files[4].getBytes());
        }
        if (files[5] != null && !files[5].isEmpty()) {
            serviceProvider.setBankStatement(files[5].getBytes());
        }
    }

}
