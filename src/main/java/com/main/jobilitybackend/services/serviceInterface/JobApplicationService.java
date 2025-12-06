package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface JobApplicationService {
    DataResponse addJobApplication(String token,String jobId)throws Exception;
    DataResponse getAllJobAllicationByJobId(String jobId)throws Exception;
    DataResponse getStudentAppliedJobs(String Email)throws Exception;

    DataResponse addInternshipApplication(String token, String internshipId) throws Exception;
    DataResponse getAllInternshipApplicationsByInternshipId(String internshipId) throws Exception;
    DataResponse getStudentAppliedInternships(String email) throws Exception;

    SuccessResponse deleteJobApplication(Long id) throws Exception;
    DataResponse getAllApplications(
            int page,
            String query,
            String date,
            String status,
            String companyName,
            String location
    ) throws Exception;

    DataResponse getAllInternshipApplications(int page, String query, String date, String status, String companyName,
            String location) throws Exception;

    DataResponse getAllJobApplications(int page, String query, String date, String status, String companyName,
            String location) throws Exception;
}
