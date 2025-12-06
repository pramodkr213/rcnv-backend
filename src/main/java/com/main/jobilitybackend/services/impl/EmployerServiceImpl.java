package com.main.jobilitybackend.services.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.UnauthorizeException;
import com.main.jobilitybackend.controllers.AdminController;
import com.main.jobilitybackend.dto.requestDTO.CompanyVarificationRequest;
import com.main.jobilitybackend.dto.requestDTO.EmployerRegisterRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.dto.responses.employer.EmployerDashboard;
import com.main.jobilitybackend.dto.responses.employer.EmployerResponse;
import com.main.jobilitybackend.dto.responses.employer.EmplyerDetailResponse;
import com.main.jobilitybackend.entities.Employer;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.jwtSecurity.CustomEmployerDetail;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.EmployerRepo;
import com.main.jobilitybackend.services.serviceInterface.EmployerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployerServiceImpl implements EmployerService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployerServiceImpl.class);

    @Autowired
    private EmployerRepo employerRepo;

    @Autowired
    private CustomEmployerDetail customEmployerDetail;

    @Autowired
    private ImageUploader imageUploader;

    @Override
    public DataResponse addEmployer(EmployerRegisterRequest request) throws Exception {
        Employer existEmployer = this.employerRepo.findByEmailAndDeleteAtIsNull(request.getEmail()).orElse(null);
        if (existEmployer != null) {
            throw new DuplicateEntityException("Admin Already Exists");
        }
        Employer employer = new Employer();
        employer.setEmail(request.getEmail());
        employer.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        employer.setDesignation(request.getDesignation());
        employer.setFirstName(request.getFirstName());
        employer.setLastName(request.getLastName());
        employer.setPhone(request.getPhone());
        employer = this.employerRepo.save(employer);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Admin Created Successfully")
                .timestamp(Instant.now())
                .data(employer)
                .build();
        return response;
    }

    @Override
    public String employerLogin(String email, String password) throws Exception {
        logger.info("Admin Loginmg with email: {}", email);
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email).orElse(null);
        if (employer == null) {
            logger.error("Invalid email or password");
            throw new UnauthorizeException("Invalid Email or Password");
        }
        UserDetails userDetails = this.customEmployerDetail.loadUserByUsername(email);
        boolean isPasswordValid = new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
        if (!isPasswordValid) {
            logger.error("Invalid email or password");
            throw new UnauthorizeException("Invalid Email or Password");
        }

        Authentication authentication = authenticate(email, password);
        String token = JwtProvider.generateJwtToken(authentication);
        logger.info("Token generated successfully");
        return token;
    }

    @Override
    public DataResponse getEmployerById(Long id) throws Exception {
        Employer employer = this.employerRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        if (employer == null) {
            throw new DuplicateEntityException("employer not found");
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Employer Found Successfully")
                .timestamp(Instant.now())
                .data(employer)
                .build();
        return response;
    }

    @Override
    public DataResponse getEmployerByEmail(String email) throws Exception {
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        if (employer == null) {
            throw new DuplicateEntityException("employer not found");
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Employer Found Successfully")
                .timestamp(Instant.now())
                .data(employer)
                .build();
        return response;
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.customEmployerDetail.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException("bad credentials");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public DataResponse addCompanyVarification(CompanyVarificationRequest request, String email, MultipartFile logo,
            MultipartFile document) throws Exception {
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        if (employer == null) {
            throw new DuplicateEntityException("employer not found");
        }
        try {
            if (logo != null && !logo.isEmpty()) {
                String logoPath = imageUploader.imageUploader(logo);
                employer.setLogoUrl(logoPath);
            }
        } catch (Exception e) {
            employer.setLogoUrl(null);
        }
        try {
            if (document != null && !document.isEmpty()) {
                String documentUrl = imageUploader.documentUploader(document);
                employer.setDocumentUrl(documentUrl);
            }
        } catch (Exception e) {
            employer.setDocumentUrl(null);
        }
        employer.setCompanyName(request.getCompanyName());
        employer.setDiscription(request.getDiscription());
        employer.setCity(request.getCity());
        employer.setIndustryType(request.getIndustryType());
        employer.setNoEmployees(request.getNoEmployees());
        employer.setWebsite(request.getWebsite());
        employer.setSmLink(request.getSmLink());
        employer.setDocument(true);
        this.employerRepo.save(employer);

        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Company Varification Request Created Successfully")
                .timestamp(Instant.now())
                .data(employer)
                .build();
        return response;
    }

    @Override
    public DataResponse getPendingEmployerRequest(int page, String keywords, String companyName, String city,
            String industryType) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
       
        logger.info("Fetching pending employer requests | page={}, keywords={}, companyName={}, city={}, industryType={}",
                page, keywords, companyName, city, industryType);
        List<EmployerResponse> pendingEmployers = this.employerRepo.findPendingEmployersRequest(keywords, companyName,
                city, industryType, pageable);
        
       long totalCount = this.employerRepo.countPendingEmployersRequest(keywords, companyName, city, industryType);

       logger.info("[Service] Found {} pending employer(s) out of total {}", 
               pendingEmployers.size(), totalCount);
       
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .totalCount(totalCount)
                .message("Pending Employer Requests Found Successfully")
                .timestamp(Instant.now())
                .data(pendingEmployers)
                .build();
        return response;
    }

    @Override
    public DataResponse getEmployerDashboard(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromJwt(jwt);
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        if (employer == null) {
            throw new DuplicateEntityException("employer not found");
        }

        EmployerDashboard employerDashboard = this.employerRepo.findEmployerDashboard(employer.getId());
        
        long totalCount = this.employerRepo.countJobPosts(employer.getId());
        
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .totalCount(totalCount)
                .message("Employer Dashboard Found Successfully")
                .timestamp(Instant.now())
                .data(employerDashboard)
                .build();
        return response;
    }

    @Override
    public DataResponse getEmployers(int page, String keywords, String companyName, String city, String industryType,
            String dateStr) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        
        logger.info("[Service] Fetching employers | page={}, keywords='{}', companyName='{}', city='{}', industryType='{}', date='{}'",
                page, keywords, companyName, city, industryType, dateStr);
        
        LocalDate formattedDate = null;

        logger.debug("Date string received: {}", dateStr);

        // Validate date format if present
    if (dateStr != null && !dateStr.isEmpty()) {
        try {
           
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
        	logger.error("Invalid date format: '{}'. Expected format: YYYY-MM-DD", dateStr);
            throw new IllegalArgumentException("Date must be in YYYY-MM-DD format"); 
        }
    }

        List<Map<String,String>> employers = this.employerRepo.searchEmployers(keywords, companyName, city, industryType,
                dateStr, pageable);
        Long totalCount = this.employerRepo.countFilteredEmployers(keywords, companyName, city, industryType, dateStr);
        
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .totalCount(totalCount)
                .message("Employers Found Successfully")
                //.timestamp(Instant.now())
                .employeeData(employers)
                .build();
        return response;
    }

    @Override
    public DataResponse getEmployerByIdForAdmin(Long id) throws Exception {
    	logger.info("[getEmployerByIdForAdmin Service] Request to get employer details for ID: {}", id);
    	
    	 EmplyerDetailResponse employer = this.employerRepo.getEmployerDetail(id)
    	            .orElseThrow(() -> {
    	                logger.warn("Employer not found for ID: {}", id);
    	                return new UsernameNotFoundException("Employer not found");
    	            });
    	 
        if (employer == null) {
            throw new DuplicateEntityException("employer not found");
        }

        logger.info("[Service] Employer details fetched for ID: {}", id);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Employer Found Successfully")
                .timestamp(Instant.now())
                .data(employer)
                .build();
        return response;
    }

    @Override
    public SuccessResponse varifyEmployer(Long id) throws Exception {
    	
    	logger.info("[varifyEmployer Service] Request to varifyEmployer {}", id);
        Employer employer = this.employerRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        if (employer == null) {
        	logger.info("[varifyEmployer Service] Employee Not Found {}", id);
            throw new DuplicateEntityException("employer not found");
        }

        if (employer.isVerified()) {
        	logger.info("[varifyEmployer Service] Employer already varified {}", employer.toString());
            throw new UnauthorizeException("Employer already varified");
        }

        this.employerRepo.varifyEmployer(employer.getId(), true);

        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Employer Varified Successfully")
                .timestamp(Instant.now())
                .build();
        return response;
    }

    @Override
    public SuccessResponse activeAndIncativeEmployer(Long id) throws Exception {
    	
    	logger.info("[ActiveAndIncativeEmployer Service] Request to ActiveAndIncativeEmployer {}", id);
    	
        Employer employer = this.employerRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        if (employer == null) {
        	logger.info("[ActiveAndIncativeEmployer Service] Employee Not Found {}", id);
            throw new DuplicateEntityException("employer not found");
        }

        if (employer.isActive()) {
            this.employerRepo.acitveAndInactiveEmployer(id, false);
            return SuccessResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Employer Deactivated Successfully")
                    .timestamp(Instant.now())
                    .build();
        } else {
            this.employerRepo.acitveAndInactiveEmployer(id, true);
            return SuccessResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Employer Activated Successfully")
                    .timestamp(Instant.now())
                    .build();
        }
    }

    @Override
    public SuccessResponse deleteEmployer(Long id) throws Exception {
    	
    	logger.info("[DeleteEmployer Service] Request to DeleteEmployer {}", id);
    	
        Employer employer = this.employerRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        if (employer == null) {
        	logger.info("[DeleteEmployer Service] Employee Not Found {}", id);
            throw new DuplicateEntityException("employer not found");
        }

        this.employerRepo.softDeleteEmployer(id);

        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Employer Deleted Successfully")
                .timestamp(Instant.now())
                .build();
        return response;
    }

}
