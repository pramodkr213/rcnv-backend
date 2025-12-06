package com.main.jobilitybackend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipCardResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.JobCardResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.entities.InternshipPost;
import com.main.jobilitybackend.entities.JobBookmarks;
import com.main.jobilitybackend.entities.JobPost;
import com.main.jobilitybackend.entities.Student;
import com.main.jobilitybackend.repositories.InternshipRepo;
import com.main.jobilitybackend.repositories.JobBookmarksRepo;
import com.main.jobilitybackend.repositories.JobPostRepo;
import com.main.jobilitybackend.repositories.StudentRepo;
import com.main.jobilitybackend.services.serviceInterface.JobBookmarkService;

@Service
public class JobBookmarkServiceImpl implements JobBookmarkService{

    @Autowired
    private JobBookmarksRepo jobBookmarksRepo;

    @Autowired
    private JobPostRepo jobPostRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private InternshipRepo internshipRepo;

    @Override
    public DataResponse addJobBookmark(String email, String jobId) throws Exception {
        JobPost jobPost = jobPostRepo.findById(jobId)
                .orElseThrow(() -> new Exception("Job not found with id: " + jobId));
        Student student = this.studentRepo.findByEmail(email).orElse(null);
        // Check if the job is already bookmarked
        if (jobBookmarksRepo.existsByJobIdAndStudentId(jobId, student.getId())) {

            // If it exists, remove the bookmark
            jobBookmarksRepo.deleteByJobIdAndStudentId(jobId, student.getId());
            return DataResponse.builder()
                    .success(false)
                    .status(HttpStatus.CONFLICT)
                    .statusCode(409)
                    .message("Bookmark Removed Successfully")
                    .build();
        }
        // Create a new bookmark
        JobBookmarks jobBookmark = new JobBookmarks();
        jobBookmark.setJob(jobPost);
        jobBookmark.setStudent(student);;
        jobBookmark = jobBookmarksRepo.save(jobBookmark);
        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Job bookmarked successfully")
                .data(jobBookmark)
                .build();
    }

    @Override
    public DataResponse getStudentBookMarkedJobs(String email) throws Exception {
        StudentResponse student = studentRepo.findByEmailAndDeleteAtNull(email)
                .orElseThrow(() -> new Exception("Student not found with email: " + email));
        
        // Fetch all bookmarked jobs for the student
        List<JobCardResponse> bookmarkedJobs = jobBookmarksRepo.findBookmarkedJobsByStudentId(student.getId());
        
        if (bookmarkedJobs.isEmpty()) {
            return DataResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .message("No bookmarked jobs found")
                    .build();
        }
        
        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Bookmarked jobs retrieved successfully")
                .data(bookmarkedJobs)
                .build();
    }

    @Override
    public DataResponse addInternshipBookmark(String email, String internshipId) throws Exception {
        Student student = this.studentRepo.findByEmail(email).orElse(null);
        // Check if the internship exists
        InternshipPost internshipPost = internshipRepo.findById(internshipId)
                .orElseThrow(() -> new Exception("Internship not found with id: " + internshipId));
        
        if (jobBookmarksRepo.existsByInternshipIdAndStudentId(internshipId, student.getId())) {
            
            jobBookmarksRepo.deleteByInternshipIdAndStudentId(internshipId, student.getId());
            return DataResponse.builder()
                    .success(false)
                    .status(HttpStatus.CONFLICT)
                    .statusCode(409)
                    .message("Bookmark Removed Successfully")
                    .build();
        }

        // Create a new bookmark
        JobBookmarks internshipBookmark = new JobBookmarks();
        internshipBookmark.setInternship(internshipPost);;
        internshipBookmark.setStudent(student);
        internshipBookmark = jobBookmarksRepo.save(internshipBookmark);
        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Internship bookmarked successfully")
                .data(internshipBookmark)
                .build();

    }

    @Override
    public DataResponse getStudentBookMarkedInternships(String email) throws Exception {
        StudentResponse student = studentRepo.findByEmailAndDeleteAtNull(email)
                .orElseThrow(() -> new Exception("Student not found with email: " + email));
        
        List<InternshipCardResponse> bookmarkedInternships = jobBookmarksRepo.findBookmarkedInternshipsByStudentId(student.getId());
        
        if (bookmarkedInternships.isEmpty()) {
            return DataResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .message("No bookmarked internships found")
                    .build();
        }
        
        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Bookmarked internships retrieved successfully")
                .data(bookmarkedInternships)
                .build();
    }
}
