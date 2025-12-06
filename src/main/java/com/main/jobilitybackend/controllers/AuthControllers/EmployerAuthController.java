package com.main.jobilitybackend.controllers.AuthControllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.jobilitybackend.dto.requestDTO.EmployerRegisterRequest;
import com.main.jobilitybackend.dto.requestDTO.LoginRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Employer;
import com.main.jobilitybackend.jwtSecurity.CustomEmployerDetail;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.EmployerRepo;
import com.main.jobilitybackend.services.serviceInterface.EmployerService;
import com.main.jobilitybackend.services.serviceInterface.OtpService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Slf4j
public class EmployerAuthController {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmployerRepo employerRepo;

    @Autowired
    private CustomEmployerDetail customEmployerDetail;

    @PostMapping("/register/employer")
    public ResponseEntity<DataResponse> registerEmployer(@Valid @RequestBody EmployerRegisterRequest request,
            HttpServletResponse response2)
            throws Exception {
        log.info("Registering user with email: {}", request.getEmail());

        DataResponse response = this.employerService.addEmployer(request);
        Employer employer = (Employer) response.getData();
        Cookie accessTokenCookie = new Cookie("accessToken",
                JwtProvider.generateJwtToken(authenticate(employer.getEmail(), employer.getPassword())));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(24 * 60 * 60 * 1000);
        accessTokenCookie.setAttribute("SameSite", "Strict");

        // Add cookie to response
        response2.addCookie(accessTokenCookie);
        return ResponseEntity.status(201).body(response);

    }

    @PostMapping("/login/employer")
    public ResponseEntity<DataResponse> loginEmployer(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) throws Exception {

        log.info("Logging in admin with email: {}", request.getEmail());

        String token = this.employerService.employerLogin(request.getEmail(), request.getPassword());

        Cookie accessTokenCookie = new Cookie("accessToken", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(24 * 60 * 60 * 1000);
        accessTokenCookie.setAttribute("SameSite", "Strict");

        // Add cookie to response
        response.addCookie(accessTokenCookie);

        // Build and return success response

        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(request.getEmail())
                .orElseThrow(
                        () -> new EntityNotFoundException("Employer not found with email: " + request.getEmail()));
        DataResponse successResponse = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Login Successful")
                .timestamp(Instant.now())
                .data(employer)
                .build();

        return ResponseEntity.ok(successResponse);

    }

    @PostMapping("/send-otp")
    public ResponseEntity<SuccessResponse> sendOtp(@RequestParam() String email) throws Exception {
        log.info("Sending OTP to email: {}", email);
        this.employerService.getEmployerByEmail(email).getData();
        try {
            this.otpService.generateAndSendOtp(email);
            SuccessResponse response = SuccessResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("OTP sent successfully!")
                    .timestamp(Instant.now())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<SuccessResponse> verifyEmail(@RequestParam(required = true) String email,
            @RequestParam(required = true) String otp) throws Exception {
        log.info("Verifying OTP for email: {}", email);

        try {
            if (this.otpService.verifyOtp(email, otp)) {
                log.info("OTP verified successfully for email: {}", email);
                Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email).orElse(null);
                employer.setEmailVerified(true);
                this.employerRepo.save(employer);
                SuccessResponse response = SuccessResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("OTP verified successfully!")
                        .timestamp(Instant.now())
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                throw new EntityNotFoundException("invalid OTP");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<SuccessResponse> verifyOtp(@RequestParam(required = true) String email,
            @RequestParam(required = true) String otp) throws Exception {
        log.info("Verifying OTP for email: {}", email);

        try {
            if (this.otpService.verifyOtp(email, otp)) {
                log.info("OTP verified successfully for email: {}", email);
                SuccessResponse response = SuccessResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("OTP verified successfully!")
                        .timestamp(Instant.now())
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                throw new EntityNotFoundException("invalid OTP");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse> resetPassword(@RequestParam(required = true) String email,
            @RequestParam(required = true) String otp,
            @RequestParam(required = true) String password) throws Exception {
        log.info("Resetting password for email: {}", email);

        try {

            if (this.otpService.varifySession(email, otp)) {
                log.info("Password reset successfully for email: {}", email);
                Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email).orElse(null);
                if (employer == null) {
                    throw new EntityNotFoundException("Employer not found with email: " + email);
                }
                employer.setPassword(new BCryptPasswordEncoder().encode(password));
                this.employerRepo.save(employer);
                SuccessResponse successResponse = SuccessResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Password reset successfully!")
                        .timestamp(Instant.now())
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                log.warn("Session expired or invalid OTP for email: {}", email);
                throw new EntityNotFoundException("Session expired or invalid OTP");
            }

        } catch (Exception e) {
            log.error("Error resetting password for email: {}: {}", email,
                    e.getMessage());
            throw new Exception("Error resetting password: " + e.getMessage());
        }
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.customEmployerDetail.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("bad credentials");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
