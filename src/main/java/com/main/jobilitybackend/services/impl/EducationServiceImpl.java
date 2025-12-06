package com.main.jobilitybackend.services.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.dto.requestDTO.studentRequests.EducationRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.entities.Education;
import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.repositories.EducationRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.EducationService;

@Service
public class EducationServiceImpl implements EducationService{

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private EducationRepo educationRepo;

    @Override
    public DataResponse addEducation(EducationRequest request, String email) {
        Student student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
        // Create a new Education entity from the request
        Education education = new Education();  
        education.setDegree(request.getDegree());
        education.setType(request.getType());
        education.setCollege(request.getCollege());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setYearOfPassing(request.getYearOfPassing());
        education.setStudent(student);
        // Save the education entity to the database
        education = educationRepo.save(education);
        // Return a successful response with the saved education entity
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .success(true)
                .message("Education added successfully")
                .timestamp(Instant.now())
                .data(education)
                .build();
        return response;
    }

    @Override
    public DataResponse updateEducation(EducationRequest request, Long educationId, String email) {
        // Find the student by ID
        Student student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
        // Find the education by ID
        Education education = educationRepo.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found with id: " + educationId));
        // Update the education entity with the new values from the request
        education.setDegree(request.getDegree());
        education.setType(request.getType());
        education.setCollege(request.getCollege());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setYearOfPassing(request.getYearOfPassing());
        education.setStudent(student);
        // Save the updated education entity to the database    
        education = educationRepo.save(education);
        // Return a successful response with the updated education entity
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(200)
                .success(true)
                .message("Education updated successfully")
                .timestamp(Instant.now())
                .data(education)
                .build();
        return response;

    }

    @Override
    public DataResponse deleteEducation(Long educationId, String studentId) {
        
        // Find the education by ID
       educationRepo.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found with id: " + educationId));
       
        educationRepo.deleteByIdAndStudentEmail(educationId, studentId);
        // Return a successful response indicating deletion
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(200)
                .success(true)
                .message("Education deleted successfully")
                .timestamp(Instant.now())
                .build();
        return response;
    }

    @Override
    public DataResponse getEducation(String studentId) {
        List<Education> educationList = educationRepo.findByStudentId(studentId);
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(200)
                .success(true)
                .message("Education retrieved successfully")
                .timestamp(Instant.now())
                .data(educationList)
                .build();
        return response;
    }

    @Override
    public DataResponse getEducationById(Long educationId, String studentId) {
        Education education = this.educationRepo.findByIdAndStudentId(educationId, studentId).orElseThrow(() -> new RuntimeException("Education not found with id: " + educationId));
        DataResponse response = DataResponse.builder()
                .status(HttpStatus.OK)
                .statusCode(200)
                .success(true)
                .message("Education retrieved successfully")
                .timestamp(Instant.now())
                .data(education)
                .build();
        return response;
    }
    
}
