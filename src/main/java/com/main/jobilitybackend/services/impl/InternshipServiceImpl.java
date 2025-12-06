package com.main.jobilitybackend.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.IntershipPostRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.dto.responses.internshipResponses.EmployerIntership;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipCardResponse;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipDetailResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.entities.Employer;
import com.main.jobilitybackend.entities.InternshipPost;
import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.jwtSecurity.JwtProvider;
import com.main.jobilitybackend.repositories.EmployerRepo;
import com.main.jobilitybackend.repositories.InternshipRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.IntershipService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InternshipServiceImpl implements IntershipService {

    @Autowired
    private InternshipRepo internshipRepo;

    @Autowired
    private EmployerRepo employerRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public DataResponse addIntershipPost(IntershipPostRequest request, String jwt) throws Exception {

        String email = JwtProvider.getEmailFromJwt(jwt);
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email)
                .orElseThrow(() -> new Exception("Employer not found"));

        InternshipPost internshipPost = new InternshipPost();
        String id = UUID.randomUUID().toString();
        internshipPost.setId(id);
        internshipPost.setTitle(request.getTitle());
        internshipPost.setDuration(request.getDuration());
        internshipPost.setPaid(request.isPaid());
        internshipPost.setMinStipend(request.getMinStipend());
        internshipPost.setMaxStipend(request.getMaxStipend());
        internshipPost.setMode(request.getMode());
        internshipPost.setLocation(request.getLocation());
        internshipPost.setInternshipType(request.getIntershipType());
        internshipPost.setEligibility(request.getEligibility());
        internshipPost.setSkillsRequired(request.getSkillsRequired());
        internshipPost.setNumberOfOpenings(request.getNumberOfOpenings());
        internshipPost.setImmediate(request.isImmediate());
        internshipPost.setJoinFrom(request.getJoinFrom());
        internshipPost.setJoinTo(request.getJoinTo());
        internshipPost.setApplicationDeadline(request.getApplicationDeadline());
        internshipPost.setInternshipDescription(request.getInternshipDescription());
        internshipPost.setPostedBy(request.getPostedBy());
        internshipPost.setActive(true);
        internshipPost.setEmployer(employer);

        this.internshipRepo.save(internshipPost);

        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Internship post created successfully")
                .timestamp(Instant.now())

                .build();
    }

    @Override
    public DataResponse getAllEmployerIntershipPost(String token) throws Exception {
        String email = JwtProvider.getEmailFromJwt(token);
        Employer employer = this.employerRepo.findByEmailAndDeleteAtIsNull(email)
                .orElseThrow(() -> new Exception("Employer not found"));

        if (employer == null) {
            throw new Exception("Employer not found");
        }

        List<EmployerIntership> internshipPosts = this.internshipRepo
                .findByEmployerIdAndDeleteAtIsNull(employer.getId());
        
       long totalCount =  this.internshipRepo.countInternshipsByEmployerIdAndDeleteAtIsNull(employer.getId());

        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .totalCount(totalCount)
                .message("Internship posts retrieved successfully")
                .data(internshipPosts)
                .build();
    }

    @Override
    public DataResponse getIntershipPostById(String id,String email) throws Exception {
        Student student = this.studentRepo.findByEmail(email).orElse(null);
        InternshipDetailResponse internshipPost = null;
        if (student == null) {
            internshipPost = this.internshipRepo.findByIdAndDeleteAtIsNullAndIsActiveTrue(id,null)
                .orElseThrow(() -> new EntityNotFoundException("Internship post not found"));
        } else {
            internshipPost = this.internshipRepo.findByIdAndDeleteAtIsNullAndIsActiveTrue(id,student.getId())
                .orElseThrow(() -> new EntityNotFoundException("Internship post not found"));
        }
        
        Long totalCount = this.internshipRepo.countFilteredInternshipPostsNative(
        	    null, null, null, null, null, null, null, null);
        

        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .totalCount(totalCount)
                .message("Internship post retrieved successfully")
                .data(internshipPost)
                .build();
    }

//    @Override
//    public DataResponse getAllInterships(int page, String title, String location, String intershipType, Long minStipend,
//            Long maxStipend, Boolean isPaid, String mode, String email) throws Exception {
//
//        Pageable pageable = PageRequest.of(page, 10);
//        StudentResponse studentResponse = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
//        List<InternshipCardResponse> internships = new ArrayList<>();
//        if (studentResponse == null) {
//            internships = this.internshipRepo.findFilteredInternshipPosts(
//                    null, title, location, intershipType, minStipend, maxStipend, isPaid, mode, pageable);
//        } else {
//            internships = this.internshipRepo.findFilteredInternshipPosts(
//                    studentResponse.getId(), title, location, intershipType, minStipend, maxStipend, isPaid, mode,
//                    pageable); 
//        }
//
//        return DataResponse.builder()
//                .success(true)
//                .status(HttpStatus.OK)
//                .statusCode(200)
//                .message("Internship posts retrieved successfully")
//                .timestamp(Instant.now())
//                .data(internships)
//                .build();
//
//    }
    
    @Override
    public DataResponse getAllInterships(
            int page,
            String title,
            String location,
            String intershipType,
            Long minStipend,
            Long maxStipend,
            Boolean isPaid,
            String mode,
            String email) throws Exception {

        int limit = 10;
        int offset = page * limit;

        StudentResponse studentResponse = this.studentRepo.findByEmailAndDeleteAtNull(email).orElse(null);
        String studentId = studentResponse != null ? studentResponse.getId() : null;

        // Fetch paginated internship list
        List<InternshipCardResponse> internships = this.internshipRepo.findFilteredInternshipPostsNative(
                studentId, title, location, intershipType, minStipend, maxStipend, isPaid, mode, limit, offset
        );

        Long totalCount = this.internshipRepo.countFilteredInternshipPostsNative(
        	    studentId, title, location, intershipType, minStipend, maxStipend, isPaid, mode);

        // Return response
        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Internship posts retrieved successfully")
                .timestamp(Instant.now())
                .totalCount(totalCount)
                .data(internships)
                .build();
    }



    @Override
    public DataResponse getAllInterships(int page, String title, String location, String companyName, String date)
                throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        List<InternshipCardResponse> internships = this.internshipRepo.findAllInternships(
                title, location, companyName, date, pageable);
        
        long totalCount = this.internshipRepo.countFilteredInternships(title, location, companyName, date);
         
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .totalCount(totalCount)
                .message("Internship posts retrieved successfully")
                .timestamp(Instant.now())
                .data(internships)
                .build();
        return response;
    }

    @Override
    public SuccessResponse activeAndInactiveIntershipPost(String id) throws Exception {
        InternshipPost internshipPost = this.internshipRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Internship post not found"));

        if(internshipPost.isActive()){
                this.internshipRepo.activeAndInactive(id, false);
                return SuccessResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Internship post deactivated successfully")
                        .timestamp(Instant.now())
                        .build();
        }else {
                this.internshipRepo.activeAndInactive(id, true);
                return SuccessResponse.builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Internship post activated successfully")
                        .timestamp(Instant.now())
                        .build();
        }
    }

    @Override
    public SuccessResponse deleteIntershipPost(String id) throws Exception {
        this.internshipRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Internship post not found"));

        this.internshipRepo.softDeleteInternshipPostById(id);

        return SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Internship post deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public DataResponse getRecentTop5Interships() throws Exception {
       Pageable pageable = PageRequest.of(0, 5);
        List<InternshipCardResponse> internships = this.internshipRepo.findTop5RecentInternships(pageable);

        if (internships.isEmpty()) {
            return DataResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("No recent internships found")
                    .timestamp(Instant.now())
                    .data(new ArrayList<>())
                    .build();
        }

        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Recent top 5 internships retrieved successfully")
                .timestamp(Instant.now())
                .data(internships)
                .build();
    }
    
    
    @Override
    public DataResponse updateIntershipPost(String id, IntershipPostRequest request) throws Exception {
        InternshipPost internshipPost = this.internshipRepo.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Internship post not found"));
        

        if(request.getTitle()==null && request.getIsActive()==false) {
        	internshipPost.setActive(false);
        	internshipPost = internshipRepo.save(internshipPost);
        	  return DataResponse.builder()
                      .status(HttpStatus.OK)
                      .statusCode(HttpStatus.OK.value())
                      .success(true)
                      .message("Job post updated successfully")
                      .timestamp(Instant.now())
                      .data(internshipPost)
                      .build();

        }else  if(request.getTitle()==null && request.getIsActive()==true) {
        	internshipPost.setActive(true);
        	internshipPost = internshipRepo.save(internshipPost);
       	  return DataResponse.builder()
                     .status(HttpStatus.OK)
                     .statusCode(HttpStatus.OK.value())
                     .success(true)
                     .message("Job post updated successfully")
                     .timestamp(Instant.now())
                     .data(internshipPost)
                     .build();

       }

        internshipPost.setTitle(request.getTitle());
        internshipPost.setDuration(request.getDuration());
        internshipPost.setPaid(request.isPaid());
        internshipPost.setMinStipend(request.getMinStipend());
        internshipPost.setMaxStipend(request.getMaxStipend());
        internshipPost.setMode(request.getMode());
        internshipPost.setLocation(request.getLocation());
        internshipPost.setInternshipType(request.getIntershipType());
        internshipPost.setEligibility(request.getEligibility());
        internshipPost.setSkillsRequired(request.getSkillsRequired());
        internshipPost.setNumberOfOpenings(request.getNumberOfOpenings());
        internshipPost.setImmediate(request.isImmediate());
        internshipPost.setJoinFrom(request.getJoinFrom());
        internshipPost.setJoinTo(request.getJoinTo());
        internshipPost.setApplicationDeadline(request.getApplicationDeadline());
        internshipPost.setInternshipDescription(request.getInternshipDescription());
        internshipPost.setPostedBy(request.getPostedBy());

        this.internshipRepo.save(internshipPost);

        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Internship post updated successfully")
                .timestamp(Instant.now())
                .data(internshipPost)
                .build();
    }

}
