package com.project.spring.controller;


import com.project.spring.entity.UserRole;
import com.project.spring.repository.UserRepository;
import com.project.spring.repository.UserRoleRepository;
import com.project.spring.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.project.spring.entity.UserInfo;
import com.project.spring.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuthController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private EmailService emailService;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private boolean isTokenValid(LocalDateTime expirationTime) {
        // Check time still verify
        return expirationTime.isAfter(LocalDateTime.now());
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
    public void createVerifiedUser(UserInfo user) {
        // Create token
        String verificationToken = generateVerificationToken();

        // set time expired 5'
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        // set token and time expired for user
        user.setVerificationToken(verificationToken);
        user.setExpirationTime(expirationTime);
        String urlAPI = "http://localhost:8080/api/auth/verify/";
        emailService.sendVerificationEmail(user.getUsername(),user.getEmail(),urlAPI+verificationToken );
    }


    @GetMapping("/verify/{verificationToken}")
    public ResponseEntity<String> testapi(@PathVariable String verificationToken) {

        UserInfo user = userRepository.findByVerificationToken(verificationToken);
        if (user != null && !user.isVerified() && isTokenValid(user.getExpirationTime())) {
            user.setVerified(true);
            user.setVerificationToken(null);
            user.setExpirationTime(null);
            userRepository.save(user);
            return ResponseEntity.ok("Account verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token.");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserInfo> registerUser(@RequestBody UserInfo userInfo) {
        Optional<UserInfo> existingUser = userRepository.findByUsername(userInfo.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        if (userInfo.getRole() == null) {
            UserRole defaultRole = userRoleRepository.findByRoleName("ROLE_USER").orElse(null);
            userInfo.setRole(defaultRole);
        }

        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        createVerifiedUser(userInfo);
        UserInfo savedUser = userRepository.save(userInfo);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginAndGenerateToken(@RequestBody UserInfo user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication failed");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

}