package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;

public interface JobBookmarkService {
    DataResponse addJobBookmark(String email, String jobId) throws Exception;
    DataResponse getStudentBookMarkedJobs(String email) throws Exception;

    DataResponse addInternshipBookmark(String email, String internshipId) throws Exception; 
    DataResponse getStudentBookMarkedInternships(String email) throws Exception;
}
