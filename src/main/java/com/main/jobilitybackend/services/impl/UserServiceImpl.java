package com.main.jobilitybackend.services.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentProfileResponse;
import com.main.jobilitybackend.entities.Admin;
import com.main.jobilitybackend.entities.Employer;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.AdminRepo;
import com.main.jobilitybackend.repositories.EmployerRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private EmployerRepo employerRepo;

    @Override
    public DataResponse getProfile(String token) throws Exception {

        String role = JwtProvider.getRoleFromJwt(token);
        if (role.equals("ROLE_ADMIN")) {
            String email = JwtProvider.getEmailFromJwt(token);
            Admin admin = this.adminRepo.findByEmail(email).orElse(null);
            if (admin != null) {
                DataResponse response = DataResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Admin Profile")
                        .timestamp(Instant.now())
                        .data(admin)
                        .build();
                return response;
            }
        }
        if (role.equals("ROLE_STUDENT")) {
            String email = JwtProvider.getEmailFromJwt(token);
            StudentProfileResponse student = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
            if (student != null) {
                DataResponse response = DataResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Student Profile")
                        .timestamp(Instant.now())
                        .data(student)
                        .build();
                return response;
            }
        }
        if (role.equals("ROLE_EMPLOYER")) {
            String email = JwtProvider.getEmailFromJwt(token);
            Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email).orElse(null);
            if (employer != null) {
                DataResponse response = DataResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Employer Profile")
                        .timestamp(Instant.now())
                        .data(employer)
                        .build();
                return response;
            }
        }
        throw new Exception("User not found");

    }
}
