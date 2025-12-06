package com.main.jobilitybackend.dto.responses.studentResponses;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class StudentDataResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private Instant timestamp;
    private T data; // generic type for single object
    private List<T> studentData; // optional, if you want to return list of students
    private Map<String, Object> additionalData; // optional extra data
}
