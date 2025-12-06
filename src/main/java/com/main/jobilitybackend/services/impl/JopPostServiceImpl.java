package com.main.jobilitybackend.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.JobPostRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.EmployerJobs;
import com.main.jobilitybackend.dto.responses.jobReponses.JobCardResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.JobDetailResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.entities.Employer;
import com.main.jobilitybackend.entities.JobPost;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.EmployerRepo;
import com.main.jobilitybackend.repositories.JobPostRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.JobPostService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class JopPostServiceImpl implements JobPostService {

    @Autowired
    private JobPostRepo jopPostRepo;

    @Autowired
    private EmployerRepo employerRepo;

    @Autowired
    private StudentRepo studentRepo;

    
    private static final Logger logger = LoggerFactory.getLogger(JopPostServiceImpl.class);
    
    @Override
    public DataResponse addJobPost(JobPostRequest request, String jwt) throws Exception {

        // Extract employer from JWT token
        String email = JwtProvider.getEmailFromJwt(jwt);
        Employer employer = employerRepo.findByEmailAndDeleteAtIsNull(email)
                .orElseThrow(() -> new EntityNotFoundException("Employer not found"));

        // Generate unique ID
        String id = UUID.randomUUID().toString().replace("-", "");

        // Build JobPost object
        JobPost jobPost = JobPost.builder()
                .id(id)
                .title(request.getTitle())
                .description(request.getDescription())
                .isFresher(request.isFresher())
                .minExperience(request.getMinExperience())
                .maxExperience(request.getMaxExperience())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .mode(request.getMode())
                .skills(request.getSkills())
                .lastDate(request.getLastDate())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .numOfWorkingDays(request.getNumOfWorkingDays())
                .otherSalaryDetails(request.getOtherSalaryDetails())
                .numberOfVacancies(request.getNumberOfVacancies())
                .postedBy(request.getPostedBy())
                .active(true)
                .employer(employer)
                .sector(request.getSector())
                .build();

        // Save to repository
        jobPost = jopPostRepo.save(jobPost);

        // Create success response
        return DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job post added successfully")
                .timestamp(Instant.now())
                .data(jobPost)
                .build();

    }

    @Override
    public DataResponse getJobPostById(String id) throws Exception {

        JobPost jobPost = jopPostRepo.findById(id).orElse(null);
        if (jobPost == null) {
            throw new Exception("Job post not found");
        }

        // Build and return response
        DataResponse dataResponse = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job post retrieved successfully")
                .timestamp(Instant.now())
                .data(jobPost)
                .build();

        return dataResponse;

    }

    @Override
    public DataResponse getJobsByEmployerId(String jwt,String location,String title) throws Exception {

        Employer employer = employerRepo.findByEmailAndDeleteAtIsNull(JwtProvider.getEmailFromJwt(jwt))
                .orElse(null);
        if (employer == null) {
            throw new EntityNotFoundException("Employer not found");
        }
        List<EmployerJobs> jobPosts = jopPostRepo.findByEmployerIdAndDeleteAtIsNull(employer.getId(),location,title);
        
       long totalCount = jopPostRepo.countByEmployerWithFiltersNative(employer.getId(), location, title);
        
        
        DataResponse dataResponse = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .totalCount(totalCount)
                .message("Job posts retrieved successfully")
                .timestamp(Instant.now())
                .data(jobPosts)
                .build();
        return dataResponse;

    }

    @Override
    public DataResponse updateJobPost(String id, JobPostRequest request) throws Exception {

        JobPost jobPost = this.jopPostRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found"));

  
        if(request.getTitle()==null && request.getActive()==false) {
        	jobPost.setActive(false);
        	 jobPost = jopPostRepo.save(jobPost);
        	  return DataResponse.builder()
                      .status(HttpStatus.OK)
                      .statusCode(HttpStatus.OK.value())
                      .success(true)
                      .message("Job post updated successfully")
                      .timestamp(Instant.now())
                      .data(jobPost)
                      .build();

        }else  if(request.getTitle()==null && request.getActive()==true) {
        	jobPost.setActive(true);
       	 jobPost = jopPostRepo.save(jobPost);
       	  return DataResponse.builder()
                     .status(HttpStatus.OK)
                     .statusCode(HttpStatus.OK.value())
                     .success(true)
                     .message("Job post updated successfully")
                     .timestamp(Instant.now())
                     .data(jobPost)
                     .build();

       }

        
        // Update fields from request
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setFresher(request.isFresher());
        jobPost.setMinExperience(request.getMinExperience());
        jobPost.setMaxExperience(request.getMaxExperience());
        jobPost.setLocation(request.getLocation());
        jobPost.setJobType(request.getJobType());
        jobPost.setMode(request.getMode());
        jobPost.setSkills(request.getSkills());
        jobPost.setMinSalary(request.getMinSalary());
        jobPost.setMaxSalary(request.getMaxSalary());
        jobPost.setNumOfWorkingDays(request.getNumOfWorkingDays());
        jobPost.setOtherSalaryDetails(request.getOtherSalaryDetails());
        jobPost.setNumberOfVacancies(request.getNumberOfVacancies());
        jobPost.setPostedBy(request.getPostedBy());
        jobPost.setActive(!jobPost.isActive());

        // Save updated job post
        jobPost = jopPostRepo.save(jobPost);

        return DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job post updated successfully")
                .timestamp(Instant.now())
                .data(jobPost)
                .build();

    }

//    @Override
//    public DataResponse getAllJobPosts(
//            int page,
//            String title,
//            String location,
//            String jobType,
//            String mode,
//            Integer minExperience,
//            Integer maxExperience,
//            Boolean isFresher,
//            Long minSalary,
//            Long maxSalary,
//            String email) throws Exception {
//
//        Pageable pageable = PageRequest.of(page, 10);
//        StudentResponse studentResponse = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
//        List<JobCardResponse> jobs = new ArrayList<>();
//        if (studentResponse == null) {
//            jobs = this.jopPostRepo.findFilteredJobPosts(
//                    null, title, location, jobType, mode, minExperience, maxExperience, isFresher, minSalary, maxSalary,
//                    pageable);
//
//        } else {
//            jobs = this.jopPostRepo.findFilteredJobPosts(
//                    studentResponse.getId(), title, location, jobType, mode, minExperience, maxExperience, isFresher,
//                    minSalary, maxSalary, pageable);
//        }
//        DataResponse response = DataResponse.builder()
//                .status(HttpStatus.OK)
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Get jobs successfully")
//                .timestamp(Instant.now())
//                .data(jobs)
//                .build();
//
//        return response;
//
//    }
    
    @Override
    public DataResponse getAllJobPosts(
            int page,
            String title,
            String location,
            String jobType,
            String mode,
            Integer minExperience,
            Integer maxExperience,
            Boolean isFresher,
            Long minSalary,
            Long maxSalary,
            String email,
            String sector) throws Exception {

        int pageSize = 10;
        int offset = page * pageSize;
        long totalCount = 0;

        StudentResponse studentResponse = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
        List<JobCardResponse> jobs;

        if (studentResponse == null) {
            jobs = this.jopPostRepo.findFilteredJobPostsNative(
                    null, title, location, jobType, mode, minExperience, maxExperience,
                    isFresher, minSalary, maxSalary, pageSize, offset,sector);
            totalCount = this.jopPostRepo.countFilteredJobPostsNative(email, title, location, jobType, mode, minExperience, maxExperience, isFresher, minSalary, maxSalary,sector);
        } else {
            jobs = this.jopPostRepo.findFilteredJobPostsNative(
                    studentResponse.getId(), title, location, jobType, mode, minExperience,
                    maxExperience, isFresher, minSalary, maxSalary, pageSize, offset,sector);
            totalCount = this.jopPostRepo.countFilteredJobPostsNative(email, title, location, jobType, mode, minExperience, maxExperience, isFresher, minSalary, maxSalary,sector);
        }

        return DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .totalCount(totalCount)
                .message("Get jobs successfully")
                .timestamp(Instant.now())
                .data(jobs)
                .build();
    }


    @Override
    public DataResponse getJobPostNotDeletedById(String id, String email) throws Exception {
        StudentResponse studentResponse = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
        JobDetailResponse job = null;
        if (studentResponse == null) {
            job = this.jopPostRepo.findByIdAndDeleteAtIsNull(id, null).orElse(null);
        } else {
            job = this.jopPostRepo.findByIdAndDeleteAtIsNull(id, studentResponse.getId()).orElse(null);
        }
        if (job == null) {
            throw new EntityNotFoundException("Exception");
        }
        
        long totalCount = this.jopPostRepo.countFilteredJobPostsNative(id, email, email, email, id, null, null, null, null, null, null);
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .totalCount(totalCount)
                .message("Get job Detail successfully")
                .timestamp(Instant.now())
                .data(job)
                .build();
        return response;

    }

    @Override
    public DataResponse getAllJobsForAdmin(int page, String title, String location, String companyName, String date)
            throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        
        logger.info("[JobPostService:getAllJobsForAdmin] Filters => title: {}, location: {}, company: {}, date: {}",
                title, location, companyName, date);

        List<JobDetailResponse> jobs = this.jopPostRepo.findFilteredJobPosts(title, location, companyName, date,
                pageable);
        Long totalCount = this.jopPostRepo.countFilteredJobPosts(title, location, companyName, date);
        
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .totalCount(totalCount)
                .success(true)
                .message("Get jobs successfully")
                .timestamp(Instant.now())
                .data(jobs)
                .build();
        return response;
    }

    @Override
    public SuccessResponse activeAndInactiveJobPost(String id) throws Exception {
    	
    	
        JobPost jobPost = jopPostRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found"));

        if (jobPost.isActive()) {
            this.jopPostRepo.activeAndDeactivateJobPostById(id, false);
            return SuccessResponse.builder()
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Job post deactivated successfully")
                    .timestamp(Instant.now())
                    .build();
        } else {
            this.jopPostRepo.activeAndDeactivateJobPostById(id, true);
            return SuccessResponse.builder()
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Job post activated successfully")
                    .timestamp(Instant.now())
                    .build();
        }
    }

    @Override
    public SuccessResponse deleteJobPost(String id) throws Exception {
        jopPostRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found"));

        // Soft delete the job post
        this.jopPostRepo.softDeleteJobPostById(id);

        return SuccessResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job post deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public DataResponse getRecentTop5Jobs() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        List<JobCardResponse> jobs = jopPostRepo.findTop5MostRecentJobs(pageable);
        return DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Recent top 5 jobs retrieved successfully")
                .timestamp(Instant.now())
                .data(jobs)
                .build();
    }

}
