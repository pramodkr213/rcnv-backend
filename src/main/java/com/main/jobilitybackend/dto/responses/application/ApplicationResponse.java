package com.main.jobilitybackend.dto.responses.application;

import java.time.LocalDateTime;


public interface ApplicationResponse {
    LocalDateTime getAppliedAt();
    Long getId();
    String getStatus();

    StudentInfo getApplicant();

    public interface  StudentInfo {
        String getId();
        String getFirstName();
        String getLastName();
        String getEmail();
        String getPhone();
        String getProfilePicture();
        String getCity();
        String getResumeLink();

    }

}
