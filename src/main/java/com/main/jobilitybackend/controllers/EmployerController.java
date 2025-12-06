package com.main.jobilitybackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.CompanyVarificationRequest;
import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.IntershipPostRequest;
import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.JobPostRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.services.serviceInterface.EmployerService;
import com.main.jobilitybackend.services.serviceInterface.IntershipService;
import com.main.jobilitybackend.services.serviceInterface.JobApplicationService;
import com.main.jobilitybackend.services.serviceInterface.JobPostService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/employer")
@CrossOrigin
@Slf4j
public class EmployerController {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private EmployerService employerService;;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private IntershipService intershipService;

    @PostMapping("/jobpost")
    public ResponseEntity<DataResponse> addJobPost(@RequestBody JobPostRequest request, HttpServletRequest request2)
            throws Exception {
        log.info("Adding job post with title: {}", request.getTitle());
        try {
            Cookie[] cookies = request2.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            DataResponse response = jobPostService.addJobPost(request, token);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/jobpost")
    public ResponseEntity<DataResponse> getJobPost(HttpServletRequest request,  @RequestParam(name = "location", required = false) String location,
      @RequestParam(name = "title", required = false) String title) throws Exception {
        log.info("Getting job post");
        try {
            Cookie[] cookies = request.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            DataResponse response = jobPostService.getJobsByEmployerId(token,location,title);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/jobpost/{id}")
    public ResponseEntity<DataResponse> getJobPostById(@PathVariable("id") String id, HttpServletRequest request)
            throws Exception {

        try {
            DataResponse response = jobPostService.getJobPostById(id);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/jobpost/{id}")
    public ResponseEntity<DataResponse> updateJobPost(@PathVariable("id") String id,
            @RequestBody JobPostRequest request) throws Exception {
        try {
            DataResponse response = jobPostService.updateJobPost(id, request);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
 //  Internship update API
    @PutMapping("/internshippost/{id}")
    public ResponseEntity<DataResponse> updatePostInternship(
            @PathVariable("id") String id,
            @RequestBody IntershipPostRequest request) throws Exception {
        try {
            DataResponse response = intershipService.updateIntershipPost(id, request);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    

    @GetMapping("/job/{jobId}/applicatioins")
    public ResponseEntity<DataResponse> getJobApplications(@PathVariable("jobId") String jobId) throws Exception {
        DataResponse response = this.jobApplicationService.getAllJobAllicationByJobId(jobId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/add-company")
    public ResponseEntity<DataResponse> addCompany(@RequestPart(name = "logo", required = false) MultipartFile logo,
            @RequestPart(name = "document", required = false) MultipartFile document,
            @RequestPart("company") CompanyVarificationRequest request,
            HttpServletRequest request1) throws Exception {
        log.info("Adding company");
        try {
            Cookie[] cookies = request1.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            String email = JwtProvider.getEmailFromJwt(token);
            if (request.getCompanyName() == null || request.getCompanyName().isEmpty()) {
                throw new Exception("Company name is required");
            }
            DataResponse response = this.employerService.addCompanyVarification(request, email, logo, document);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/internshipPost")
    public ResponseEntity<DataResponse> addInternshipPost(@RequestBody IntershipPostRequest request,
            HttpServletRequest request2)
            throws Exception {
        log.info("Adding internship post with title: {}", request.getTitle());
        try {
            Cookie[] cookies = request2.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            DataResponse response = intershipService.addIntershipPost(request, token);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/internshipPosts")
    public ResponseEntity<DataResponse> getInternshipPost(HttpServletRequest request) throws Exception {
        log.info("Getting internship post");

        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        DataResponse response = intershipService.getAllEmployerIntershipPost(token);
        return ResponseEntity.status(200).body(response);

    }

    @GetMapping("/internshipPosts/{id}/applications")
    public ResponseEntity<DataResponse> getInternshipApplications(@PathVariable("id") String id) throws Exception {
        try {
            DataResponse response = this.jobApplicationService.getAllInternshipApplicationsByInternshipId(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/internshipPosts/{id}")
    public ResponseEntity<DataResponse> getInternshipPostById(@PathVariable("id") String id, HttpServletRequest request)
            throws Exception {

        try {
            DataResponse response = intershipService.getIntershipPostById(id,null);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DataResponse> getEmployerDashboard(HttpServletRequest request) throws Exception {
        log.info("Getting employer dashboard");
        try {
            Cookie[] cookies = request.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            DataResponse response = employerService.getEmployerDashboard(token);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
