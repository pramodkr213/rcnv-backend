package com.main.jobilitybackend.controllers.AuthControllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.jobilitybackend.dto.requestDTO.AdminRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.services.serviceInterface.AdminService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Slf4j
public class AdminAuthController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register/admin")
    public ResponseEntity<DataResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request)
            throws Exception {
        log.info("Registering user with email: {}", request.getEmail());
        try {
            DataResponse response = adminService.addAdmin(request);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/login/admin")
    public ResponseEntity<DataResponse> loginAdmin(
            @Valid @RequestBody AdminRegisterRequest request,
            HttpServletResponse response) throws Exception {

        log.info("Logging in admin with email: {}", request.getEmail());

        String token = adminService.adminLogin(request.getEmail(), request.getPassword());

        Cookie accessTokenCookie = new Cookie("accessToken", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(24 * 60 * 60 * 1000);
        accessTokenCookie.setAttribute("SameSite", "Lax");

        // Add cookie to response
        response.addCookie(accessTokenCookie);

        DataResponse response2 = this.adminService.getAdminByEmail(request.getEmail());
        response2.setMessage("Login Successful");
        response2.setStatus(HttpStatus.OK);
        response2.setStatusCode(200);
        response2.setTimestamp(Instant.now());
        return ResponseEntity.ok(response2);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logoutAdmin(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // Delete cookie
        accessTokenCookie.setAttribute("SameSite", "Strict");

        // Add the expired cookie to the response
        response.addCookie(accessTokenCookie);

        // Build and return logout success response
        SuccessResponse successResponse = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Logout Successful")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(successResponse);
    }

    // @PostMapping("/send-otp")
    // public ResponseEntity<SuccessResponse> sendOtp(@RequestParam() String email)
    // {
    // log.info("Sending OTP to email: {}", email);
    // User user = this.userServiceImpl.getUserByEmail(email);

    // SuccessResponse response = new SuccessResponse();

    // if (user == null) {
    // log.warn("Email not associated with any account: {}", email);
    // response.setMessage("Email is not associated with any account!");
    // response.setHttpStatus(HttpStatus.BAD_REQUEST);
    // response.setStatusCode(400);

    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    // }
    // if (!user.isApproved()) {
    // log.warn("User with email: {} is not approved", email);
    // response.setMessage("you are not approve yet!");
    // response.setHttpStatus(HttpStatus.BAD_REQUEST);
    // response.setStatusCode(400);

    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    // }

    // this.otpServiceImpl.generateAndSendOtp(email);
    // log.info("OTP sent successfully to email: {}", email);
    // response.setMessage("OTP sent successfully!");
    // response.setHttpStatus(HttpStatus.OK);
    // response.setStatusCode(200);

    // return ResponseEntity.of(Optional.of(response));
    // }

    // @PostMapping("/verify-otp")
    // public ResponseEntity<SuccessResponse> verifyOtp(@RequestParam(required =
    // true) String email,
    // @RequestParam(required = true) String otp) {
    // log.info("Verifying OTP for email: {}", email);
    // SuccessResponse response = new SuccessResponse();

    // try {
    // if (this.otpServiceImpl.verifyOtp(email, otp)) {
    // log.info("OTP verified successfully for email: {}", email);
    // response.setMessage("otp varify successfully !");
    // response.setHttpStatus(HttpStatus.OK);
    // response.setStatusCode(200);
    // return ResponseEntity.of(Optional.of(response));
    // } else {
    // log.warn("Invalid OTP for email: {}", email);
    // response.setMessage("Invalid OTP!");
    // response.setHttpStatus(HttpStatus.BAD_REQUEST);
    // response.setStatusCode(400);
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    // }

    // } catch (Exception e) {
    // log.error("Error verifying OTP for email: {}: {}", email, e.getMessage());
    // response.setMessage("Invalid OTP!");
    // response.setHttpStatus(HttpStatus.BAD_REQUEST);
    // response.setStatusCode(400);

    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    // }
    // }

    // @PostMapping("/reset-password")
    // public ResponseEntity<SuccessResponse> resetPassword(@RequestParam(required =
    // true) String email,
    // @RequestParam(required = true) String otp,
    // @RequestParam(required = true) String password) {
    // log.info("Resetting password for email: {}", email);
    // SuccessResponse response = new SuccessResponse();

    // try {

    // if (this.otpServiceImpl.resetPassword(email, otp, password)) {
    // log.info("Password reset successfully for email: {}", email);
    // response.setMessage("password reseted successfully !");
    // response.setHttpStatus(HttpStatus.OK);
    // response.setStatusCode(200);
    // return ResponseEntity.of(Optional.of(response));
    // } else {
    // log.warn("Session expired or invalid OTP for email: {}", email);
    // response.setMessage("session is expired ! or invalid otp");
    // response.setHttpStatus(HttpStatus.BAD_REQUEST);
    // response.setStatusCode(400);
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    // }

    // } catch (Exception e) {
    // log.error("Error resetting password for email: {}: {}", email,
    // e.getMessage());
    // e.printStackTrace();
    // response.setMessage(e.getMessage());
    // response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    // response.setStatusCode(500);
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request)
    // {
    // log.info("Logging in user with email: {}", request.getEmail());
    // SuccessResponse response = new SuccessResponse();
    // User user = this.userServiceImpl.getUserByEmail(request.getEmail());
    // if (user == null) {
    // log.warn("Invalid username or password for email: {}", request.getEmail());
    // response.setMessage("Invalid UserName Or Password !");
    // response.setHttpStatus(HttpStatus.UNAUTHORIZED);
    // response.setStatusCode(500);
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    // }
    // if (!user.isApproved()) {
    // log.warn("User with email: {} is not approved", request.getEmail());
    // response.setMessage("User Not Approved Yet ! please contact to App Owner !");
    // response.setHttpStatus(HttpStatus.UNAUTHORIZED);
    // response.setStatusCode(500);
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    // }
    // UserDetails userDetails =
    // this.customUserDetail.loadUserByUsername(request.getEmail());
    // boolean isPasswordValid = new
    // BCryptPasswordEncoder().matches(request.getPassword(),
    // userDetails.getPassword());
    // if (!isPasswordValid) {
    // log.warn("Invalid password for email: {}", request.getEmail());
    // response.setMessage("Invalid Password !");
    // response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    // response.setStatusCode(500);
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
    // try {

    // Authentication authentication = authenticate(user.getEmail(),
    // request.getPassword());
    // log.info("User logged in successfully with email: {}", request.getEmail());
    // String role = user.getRole().toString();
    // JwtToken token = JwtProvider.generateJwt(authentication,
    // request.getClientType());

    // return ResponseEntity.of(Optional.of(response2));
    // } catch (Exception e) {
    // throw new Exception(e.getMessage());
    // }
    // }

    // private Authentication authenticate(String email, String password) {
    // UserDetails userDetails = this.customUserDetail.loadUserByUsername(email);
    // if (userDetails == null) {
    // throw new UsernameNotFoundException("bad credentials");
    // }
    // return new UsernamePasswordAuthenticationToken(userDetails, password,
    // userDetails.getAuthorities());
    // }

    // @PostMapping("/logout")
    // public ResponseEntity<SuccessResponse> logoutUser(
    // @RequestHeader(name = "Authorization", required = false) String authHeader) {

    // SuccessResponse response = new SuccessResponse();

    // try {

    // return ResponseEntity.ok(response);
    // } catch (Exception e) {
    // throw new Exception(e.getMessage());
    // }
    // }

}
