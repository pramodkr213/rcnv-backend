package com.main.jobilitybackend.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.dto.responses.application.ApplicationForAdmin;
import com.main.jobilitybackend.dto.responses.application.ApplicationResponse;
import com.main.jobilitybackend.dto.responses.application.IntershipApplications;
import com.main.jobilitybackend.dto.responses.application.JobApplications;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipCardResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.JobCardResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.entities.InternshipPost;
import com.main.jobilitybackend.entities.JobApplication;
import com.main.jobilitybackend.entities.JobPost;
import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.enumConst.JobApplicationStatus;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.InternshipRepo;
import com.main.jobilitybackend.repositories.JobApplicationRepo;
import com.main.jobilitybackend.repositories.JobPostRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.JobApplicationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private JobPostRepo jobPostRepo;

    @Autowired
    private JobApplicationRepo applicationRepo;

    @Autowired
    private InternshipRepo intershipRepo;

    @Override
    public DataResponse addJobApplication(String token, String jobId) throws Exception {

        //JobPost jobPost = this.jobPostRepo.findById(jobId).orElse(null);
        Optional<JobPost> jobPostData = this.jobPostRepo.findById(jobId);
        
        if(!jobPostData.isPresent()){
            throw new EntityNotFoundException("Job Not Found");
        }
        JobPost jobPost = jobPostData.get();
        if (jobPost == null) {
            throw new EntityNotFoundException("Job Not Found");
        }
        StudentResponse student = this.studentRepo.findByEmailAndDeleteAtNull(JwtProvider.getEmailFromJwt(token))
                .orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("Student Not Found");
        }
        if(applicationRepo.existsByJob_IdAndApplicant_Id(jobId, student.getId())) {
            throw new DuplicateEntityException("You have already applied for this job");
        }
        JobApplication jobApplication = new JobApplication();
        jobApplication.setApplicant(this.studentRepo.findById(student.getId()).orElse(null));
        jobApplication.setJob(jobPost);
        jobApplication.setStatus(JobApplicationStatus.WAITING);
        jobApplication = this.applicationRepo.save(jobApplication);

        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("applicatin submitted Successfully")
                .timestamp(Instant.now())
                .data(jobApplication)
                .build();
        return response;

    }

    @Override
    public DataResponse getAllJobAllicationByJobId(String jobId) throws Exception {

        List<ApplicationResponse> applications = this.applicationRepo.findByJobIdAndDeleteAtIsNull(jobId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("applicatin get Successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;

    }

    @Override
    public DataResponse getStudentAppliedJobs(String email) throws Exception {

        Student student = this.studentRepo.findByEmail(email).orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("Student Not Found");
        }
        List<JobCardResponse> applications = this.applicationRepo.findAppliedJobsByStudentId(student.getId());
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("applicatin get Successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;

    }

    @Override
    public DataResponse addInternshipApplication(String token, String internshipId) throws Exception {

        Student student = this.studentRepo.findByEmail(JwtProvider.getEmailFromJwt(token)).orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("Student Not Found");
        }
        InternshipPost internship = this.intershipRepo.findById(internshipId).orElse(null);
        if (internship == null) {
            throw new EntityNotFoundException("Internship Not Found");
        }
        if(applicationRepo.existsByInternship_IdAndApplicant_Id(internshipId, student.getId())) {
            throw new DuplicateEntityException("You have already applied for this internship");
        }

        JobApplication internshipApplication = new JobApplication();
        internshipApplication.setApplicant(student);
        internshipApplication.setInternship(internship);
        internshipApplication.setStatus(JobApplicationStatus.WAITING);
        internshipApplication = this.applicationRepo.save(internshipApplication);

        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Internship application submitted Successfully")
                .timestamp(Instant.now())
                .data(internshipApplication)
                .build();
        return response;

    }

    @Override
    public DataResponse getAllInternshipApplicationsByInternshipId(String internshipId) throws Exception {

        List<ApplicationResponse> applications = this.applicationRepo.findByInternshipIdAndDeleteAtIsNull(internshipId);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Internship applications retrieved successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;

    }

    @Override
    public DataResponse getStudentAppliedInternships(String email) throws Exception {

        Student student = this.studentRepo.findByEmail(email).orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("Student Not Found");
        }
        List<InternshipCardResponse> applications = this.applicationRepo
                .findAppliedInternshipsByStudentId(student.getId());
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Internship applications retrieved successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;

    }

    @Override
    public SuccessResponse deleteJobApplication(Long id) throws Exception {
        this.applicationRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Job Application Not Found"));

        this.applicationRepo.softDeleteApplicationById(id);

        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Job application deleted successfully")
                .timestamp(Instant.now())
                .build();
        return response;
    }

    @Override
    public DataResponse getAllApplications(int page, String query, String date, String status, String companyName,
            String location) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        List<ApplicationForAdmin> applications = this.applicationRepo
                .findAllApplications(query, date, status, companyName, location, pageable);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Applications retrieved successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;
    }

    @Override
    public DataResponse getAllInternshipApplications(int page, String query, String date, String status,
            String companyName, String location) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        List<IntershipApplications> applications = this.applicationRepo
                .findAllInternshipApplications(query, date, status, companyName, location, pageable);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Internship applications retrieved successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;
    }

    @Override
    public DataResponse getAllJobApplications(int page, String query, String date, String status, String companyName,
            String location) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        List<JobApplications> applications = this.applicationRepo
                .findAllJobApplications(query, date, status, companyName, location, pageable);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Job applications retrieved successfully")
                .timestamp(Instant.now())
                .data(applications)
                .build();
        return response;
    }

    
}
