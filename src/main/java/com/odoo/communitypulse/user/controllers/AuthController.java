package com.odoo.communitypulse.user.controllers;

import com.odoo.communitypulse.jwt.JwtUtil;
import com.odoo.communitypulse.user.entity.User;
import com.odoo.communitypulse.user.repository.UserRepository;
import com.odoo.communitypulse.user.request.LoginRequest;
import com.odoo.communitypulse.user.request.OtpRequest;
import com.odoo.communitypulse.user.request.RegisterRequest;
import com.odoo.communitypulse.user.service.EmailService;
import com.odoo.communitypulse.user.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }


        try {
            String otp = otpService.generateOtp(request.getEmail());
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPhone(request.getPhone());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setOtp(otp);
            userRepository.save(user);
            emailService.sendOtpEmail(request.getEmail(), otp);

            System.out.println("OTP sent to: " + request.getEmail() + ", OTP: " + otp);

            return ResponseEntity.ok("OTP sent to email. Please verify to complete registration.");
        } catch (Exception e) {
            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Failed to send OTP. Error: " + e.getMessage());
        }

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/users/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        return ResponseEntity.ok("OTP verified successfully");
    }


    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok().body(java.util.Map.of("token", token));
    }
}
