package com.main.jobilitybackend.controllers.AuthControllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.jobilitybackend.dto.requestDTO.LoginRequest;
import com.main.jobilitybackend.dto.requestDTO.StudentRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.ChangeStudentEmail;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.jwtSecurity.CustomStudentDetail;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.jwtSecurity.Jwtconstants;
import com.main.jobilitybackend.services.serviceInterface.StudentService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Slf4j
public class StudentAuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CustomStudentDetail customStudentDetail;

    @PostMapping("/register/student")
    public ResponseEntity<DataResponse> registerStudent(@Valid @RequestBody StudentRegisterRequest request,
            HttpServletResponse response2, HttpServletRequest request2)
            throws Exception {
        log.info("Registering user with email: {}", request.getEmail());
        try {
            DataResponse response = studentService.addStudent(request);
            Student student = (Student) response.getData();
            String userAgent = request2.getHeader("User-Agent");
            long expirationTime = Jwtconstants.WEBEXPIRATION;
            if (userAgent != null) {
                if (userAgent.toLowerCase().contains("android")) {
                    expirationTime = Jwtconstants.ANDROIDEXPIRATION;
                }
            }
            Cookie accessTokenCookie = new Cookie("accessToken",
                    JwtProvider.generateJwtToken(authenticate(student.getEmail(), student.getPassword()),
                            expirationTime));
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge((int) expirationTime);
            accessTokenCookie.setAttribute("SameSite", "Strict");

            // Add cookie to response
            response2.addCookie(accessTokenCookie);
            log.info("User registered successfully with email: {}", request.getEmail());
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> getLoginInfo(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 user not found");
        }

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String name = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");

        return ResponseEntity.ok("Login successful! Welcome, " + name + " (" + email + ")");
    }

    @PostMapping("/login/student")
    public ResponseEntity<DataResponse> loginStudent(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response, HttpServletRequest request2) throws Exception {

        log.info("Logging in admin with email: {}", request.getEmail());

        String userAgent = request2.getHeader("User-Agent");
        long expirationTime = Jwtconstants.WEBEXPIRATION;
        if (userAgent != null) {
            if (userAgent.toLowerCase().contains("android")) {
                expirationTime = Jwtconstants.ANDROIDEXPIRATION;
            }
        }
        String token = studentService.studentLogin(request.getEmail(), request.getPassword(), expirationTime);

        Cookie accessTokenCookie = new Cookie("accessToken", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) expirationTime);
        accessTokenCookie.setAttribute("SameSite", "Strict");

        // Add cookie to response
        response.addCookie(accessTokenCookie);

        StudentResponse student = (StudentResponse) this.studentService.getStudentByEmail(request.getEmail())
                .getData();
        DataResponse successResponse = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Login Successful")
                .timestamp(Instant.now())
                .data(student)
                .build();

        return ResponseEntity.ok(successResponse);

    }

    @PostMapping("/changeEmail")
    public ResponseEntity<DataResponse> changeEmail(@RequestBody ChangeStudentEmail request) throws Exception {

        DataResponse response = this.studentService.changeStudentEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.customStudentDetail.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("bad credentials");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

}
