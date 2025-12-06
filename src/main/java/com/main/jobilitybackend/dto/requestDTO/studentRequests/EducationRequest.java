package com.main.jobilitybackend.dto.requestDTO.studentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationRequest {
    private String degree;
    private String type;
    private String college;
    private String fieldOfStudy;
    private String yearOfPassing;
}
