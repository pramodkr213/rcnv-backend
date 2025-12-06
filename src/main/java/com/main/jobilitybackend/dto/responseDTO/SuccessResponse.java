package com.main.jobilitybackend.dto.responseDTO;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse {
    private boolean success;
    private HttpStatus status;
    private int statusCode;
    private String message;
    private Instant timestamp;
}
