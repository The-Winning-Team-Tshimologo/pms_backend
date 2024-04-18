package com.thewinningteam.pms.auth;

import com.thewinningteam.pms.Controller.UserController;
import com.thewinningteam.pms.Repository.TokenRepository;
import com.thewinningteam.pms.Repository.UserRepository;
import com.thewinningteam.pms.emailservice.EmailService;
import com.thewinningteam.pms.emailservice.EmailTemplateName;
import com.thewinningteam.pms.exception.GlobalExceptionHandler;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.Role;
import com.thewinningteam.pms.model.Token;
import com.thewinningteam.pms.model.User;
import com.thewinningteam.pms.request.AuthenticationRequest;
import com.thewinningteam.pms.response.AuthenticationResponse;
import com.thewinningteam.pms.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getUserId())
                .email(user.getEmail())
                .roles(user.getRoles().getName().toString())
                .build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new GlobalExceptionHandler.InvalidTokenException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail((Customer) savedToken.getUser());
            throw new GlobalExceptionHandler.TokenExpiredException("Activation token has expired. Please check your " +
                    "emails for a new token it will expire in 15 minutes.");
        }

        var user = userRepository.findById(savedToken.getUser().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save((Customer) user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    void sendValidationEmail(Customer customer) throws MessagingException {
        var newToken = generateAndSaveActivationToken(customer);
        // send email
        emailService.sendEmail(customer.getEmail(),
                customer.fullName(),
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
}
