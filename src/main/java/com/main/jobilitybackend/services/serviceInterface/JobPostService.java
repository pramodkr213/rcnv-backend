package com.main.jobilitybackend.services.serviceInterface;


import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.JobPostRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface JobPostService {
    DataResponse addJobPost(JobPostRequest request, String jwt) throws Exception;

    DataResponse getJobPostById(String id) throws Exception;
    DataResponse getJobsByEmployerId(String jwt,String location,String title) throws Exception;

    DataResponse getJobPostNotDeletedById(String id,String email)throws Exception;

    DataResponse updateJobPost(String id, JobPostRequest request) throws Exception;

    DataResponse getAllJobPosts(int page,
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
            String sector) throws Exception;

    DataResponse getAllJobsForAdmin(int page,
            String title,
            String location,
            String companyName,
            String date
            ) throws Exception;

        SuccessResponse activeAndInactiveJobPost(String id) throws Exception;
        SuccessResponse deleteJobPost(String id) throws Exception;

        DataResponse getRecentTop5Jobs() throws Exception;
}
