package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.IntershipPostRequest;
import com.main.jobilitybackend.dto.requestDTO.JobRequestDto.JobPostRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface IntershipService {
    DataResponse addIntershipPost(IntershipPostRequest request, String jwt) throws Exception;

    DataResponse getAllEmployerIntershipPost(String token) throws Exception;

    DataResponse getIntershipPostById(String id,String email) throws Exception;

    DataResponse getAllInterships(int page,
            String title,
            String location,
            String intershipType,
            Long minStipend,
            Long maxStipend,
            Boolean isPaid,
            String mode,
            String email) throws Exception;

    DataResponse getAllInterships(int page,String title,String location, String companyName,String date) throws Exception;

    SuccessResponse activeAndInactiveIntershipPost(String id) throws Exception;
    SuccessResponse deleteIntershipPost(String id) throws Exception;

    DataResponse getRecentTop5Interships() throws Exception;
    
    DataResponse updateIntershipPost(String id, IntershipPostRequest request) throws Exception;
}
