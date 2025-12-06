package com.main.jobilitybackend.services.impl;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.UnauthorizeException;
import com.main.jobilitybackend.controllers.AdminController;
import com.main.jobilitybackend.dto.requestDTO.AdminRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responses.adminResponse.AdminDashboard;
import com.main.jobilitybackend.entities.Admin;
import com.main.jobilitybackend.jwtSecurity.CustomAdminDetail;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.AdminRepo;
import com.main.jobilitybackend.repositories.EmployerRepo;
import com.main.jobilitybackend.repositories.InternshipRepo;
import com.main.jobilitybackend.repositories.JobApplicationRepo;
import com.main.jobilitybackend.repositories.JobPostRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.AdminService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private CustomAdminDetail customAdminDetail;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private EmployerRepo employerRepo;

    @Autowired
    private JobPostRepo jobPostRepo;

    @Autowired
    private InternshipRepo internshipRepo;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Override
    public DataResponse addAdmin(AdminRegisterRequest request) throws Exception {
        Admin exisAdmin = this.adminRepo.findByEmail(request.getEmail()).orElse(null);
        if (exisAdmin != null) {
            throw new DuplicateEntityException("Admin Already Exists");
        }
        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        admin = this.adminRepo.save(admin);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Admin Created Successfully")
                .timestamp(Instant.now())
                .data(admin)
                .build();
        return response;
    }

    @Override
    public String adminLogin(String email, String password) throws Exception {


            log.info("Admin Loginmg with email: {}", email);
            UserDetails userDetails = this.customAdminDetail.loadUserByUsername(email);
            boolean isPasswordValid = new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
            if (!isPasswordValid) {
                log.error("Invalid email or password");
                throw new UnauthorizeException("Invalid Email or Password");
            }
            Authentication authentication = authenticate(email, password);
            String token = JwtProvider.generateJwtToken(authentication);
            log.info("Token generated successfully");
            return token;
        
    }

    @Override
    public DataResponse getAdminById(Long id) {
        Admin admin = this.adminRepo.findById(id).orElse(null);
        if (admin == null) {
            throw new UnauthorizeException("Admin not found with id: " + id);
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Admin Found")
                .timestamp(Instant.now())
                .data(admin)
                .build();
        return response;
    }

    @Override
    public DataResponse getAdminByEmail(String email) {
        Admin admin = this.adminRepo.findByEmail(email).orElse(null);
        if (admin == null) {
            throw new UnauthorizeException("Admin not found with email: " + email);
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Admin Found")
                .timestamp(Instant.now())
                .data(admin)
                .build();
        return response;
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.customAdminDetail.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("bad credentials");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public DataResponse getAdminDashboard() throws Exception {
    	
    	logger.info("[SERVICE] Admin dashboard data fetching started");
    	
        long employerCount = this.employerRepo.count();
        long studentCount = this.studentRepo.count();
    	
        AdminDashboard adminDashboard = new AdminDashboard();
        adminDashboard.setTotalEmployers(this.employerRepo.count());
        adminDashboard.setTotalStudents(this.studentRepo.count());
        adminDashboard.setTotalJobs(this.jobPostRepo.count());
        adminDashboard.setTotalInternships(this.internshipRepo.count());
        adminDashboard.setTotalInternshipApplications(this.jobApplicationRepo.countByInternship_IdIsNotNullAndJob_IdIsNull());
        adminDashboard.setTotalJobApplications(this.jobApplicationRepo.countByInternship_IdIsNullAndJob_IdIsNotNull());
        adminDashboard.setTotalPendingEmployerRequests(this.employerRepo.countPendingRequests());
        
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Admin Dashboard Data")
                .timestamp(Instant.now())
                .data(adminDashboard)
                .build();
        
        logger.info("Admin dashboard data prepared successfully with {} employers, {} students",
                employerCount, studentCount);
        
        return response;

    }

    
}
