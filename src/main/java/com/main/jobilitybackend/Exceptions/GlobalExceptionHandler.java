package com.main.jobilitybackend.Exceptions;
import java.time.Instant;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<SuccessResponse> handleEntityNotFoundException(EntityNotFoundException e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value(),e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<SuccessResponse> handleDuplicateEntityException(DuplicateEntityException e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.CONFLICT,409,e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(UnauthorizeException.class)
    public ResponseEntity<SuccessResponse> handleUnauthorizeException(UnauthorizeException e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.UNAUTHORIZED,401,e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SuccessResponse> handleRuntimeException(RuntimeException e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,500,e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<SuccessResponse> handleBadRequestException(BadRequestException e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.BAD_REQUEST,400,e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessResponse> handleGeneralException(Exception e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,500,e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<SuccessResponse> handleUnsupportedOperationException(UnsupportedOperationException e){
        SuccessResponse response = new SuccessResponse(false,HttpStatus.INTERNAL_SERVER_ERROR,500,e.getMessage(),Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}