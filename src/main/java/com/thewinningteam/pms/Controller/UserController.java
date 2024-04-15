package com.thewinningteam.pms.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.DTO.LoginDTO;
import com.thewinningteam.pms.Repository.TokenRepository;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.emailservice.EmailService;
import com.thewinningteam.pms.Service.ServiceProviderService;
import com.thewinningteam.pms.emailservice.EmailTemplateName;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.Profile;
import com.thewinningteam.pms.model.ServiceProvider;
import com.thewinningteam.pms.model.Token;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin("*")

@RequiredArgsConstructor
@RequestMapping("auth")
public class UserController {

    private final CustomerService customerService;
    private final ObjectMapper objectMapper;
    private final ServiceProviderService serviceProviderService;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.SaveCustomer(customer);
            sendValidationEmail(savedCustomer);
            return new ResponseEntity<>("Customer created successfully.", HttpStatus.CREATED);
        } catch (IllegalArgumentException | MessagingException e) {
            if (e.getMessage().equals("A customer with the same email or username already exists.")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("An error occurred.", HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void sendValidationEmail(Customer customer) throws MessagingException {
        var newToken = generateAndSaveActivationToken(customer);
        // send email
        emailService.sendEmail(customer.getEmail(),
                customer.getName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
                );
    }

    private String generateAndSaveActivationToken(Customer customer) {

        // generate a token
        
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(customer)
                .build();
        tokenRepository.save(token);
        return  generatedToken;
    }

    private String generateActivationCode(int length) {

        String CHARACTERS = "0123456789";

        Random random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            codeBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        return codeBuilder.toString();

    }

    @PostMapping("/register2")
    public ResponseEntity<String> uploadDataAndImage(@RequestParam("data") String data,
                                                     @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Customer customer = objectMapper.readValue(data, Customer.class);


            if (image != null && !image.isEmpty()) {
                customer.setProfilePicture(image.getBytes());
            }

            Customer savedCustomer = customerService.SaveCustomer(customer);
            sendValidationEmail(customer);

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

    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestBody LoginDTO loginDTO) {
        String result = customerService.Login(loginDTO);
        if (result.equals("Login successful")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        Optional<CustomerDTO> customerDTOOptional = customerService.GetCustomerById(customerId);
        return customerDTOOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long customerId) {
        customerService.DeleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("update/{customerId}")
//    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @RequestBody Customer updatedCustomer) {
//        updatedCustomer.setUserId(customerId);
//        Optional<CustomerDTO> optionalCustomer = customerService.updateCustomer(updatedCustomer);
//
//        return optionalCustomer
//                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    @PutMapping("/{customerId}")
    public ResponseEntity<String> updateCustomer2(@PathVariable Long customerId, @RequestParam("data") String data,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) {

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


    //-------------------------------------- service provider --------------------------------------------------
    @PostMapping("/SP")
    public ResponseEntity<String> registerServiceProvider(@RequestParam("data") String data,
                                                          @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                                          @RequestParam(value = "identityDocument", required = false) MultipartFile identityDocument,
                                                          @RequestParam(value = "bankStatement", required = false) MultipartFile bankStatement,
                                                          @RequestParam(value = "CriminalRecord", required = false) MultipartFile CriminalRecord,
                                                          @RequestParam(value = "resume", required = false) MultipartFile resume,
                                                          @RequestParam("profile") String profile
//                                                         , @RequestParam("WorkExperience") String WorkExperience
                                                          ) {

        try {

            ServiceProvider serviceProvider = objectMapper.readValue(data, ServiceProvider.class);
            Profile spProfile = objectMapper.readValue(profile, Profile.class);

//            System.out.println("\n serviceProvider => "  + serviceProvider);
//            System.out.println("\n profile => "  + spProfile);
//
//            System.out.println("\n name => "  + serviceProvider.getUserName());

            if (profilePicture != null && !profilePicture.isEmpty()) {
                serviceProvider.setProfilePicture(profilePicture.getBytes());
            }
            if (identityDocument != null && !identityDocument.isEmpty()) {
                serviceProvider.setIdentityDocument(identityDocument.getBytes());
            }if (bankStatement != null && !bankStatement.isEmpty()) {
                serviceProvider.setBankStatement(bankStatement.getBytes());
            }if (CriminalRecord != null && !CriminalRecord.isEmpty()) {
                serviceProvider.setCriminalRecord(CriminalRecord.getBytes());
            }if (resume != null && !resume.isEmpty()) {
                serviceProvider.setResume(resume.getBytes());
            }
            serviceProvider.setProfile(spProfile);

//            System.out.println("\n Updated-> "+serviceProvider);



            ServiceProvider savedCustomer = serviceProviderService.SaveServiceProvider(serviceProvider);
            return new ResponseEntity<>("Customer created successfully.", HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid JSON format.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("A service provider with the same email or username already exists.")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("An error occurred.", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred while processing the image.", HttpStatus.BAD_REQUEST);


        }
    }


    @DeleteMapping("/sp/{serviceProviderId}")
    public ResponseEntity<Void> updateServiceProviderAccessStatus(@PathVariable Long serviceProviderId) {
        serviceProviderService.DeleteServiceProviderById(serviceProviderId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/ss")
    public ResponseEntity<String> getCustomerByIdsdgdsg() {
        return ResponseEntity.ok("dsgdsg");
    }
}
