package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.requestDTO.studentRequests.EducationRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;

public interface EducationService {
    DataResponse addEducation(EducationRequest request, String email);
    DataResponse updateEducation(EducationRequest request, Long educationId, String email);
    DataResponse deleteEducation(Long educationId, String email);
    DataResponse getEducation(String studentId);
    DataResponse getEducationById(Long educationId, String email);
}
