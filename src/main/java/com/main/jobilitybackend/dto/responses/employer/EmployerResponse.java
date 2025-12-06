package com.main.jobilitybackend.dto.responses.employer;

import java.time.LocalDateTime;

public interface EmployerResponse {
    Long getId();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getPhone();
    String getDesignation();
    String getCompanyName();
    String getCity();
    String getIndustryType();
    boolean getIsEmailVerified();
    boolean getVerified();
    boolean getActive();
    LocalDateTime getCreatedAt();
    Long getJobPostCount();
    Long getInternshipPostCount();
}
