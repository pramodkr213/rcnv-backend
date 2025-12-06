package com.main.jobilitybackend.dto.responses.application;

public interface JobApplications extends ApplicationResponse{
    JobInfo getJob();
    CompanyInfo getCompany();

    interface JobInfo {
        String getId();
        String getTitle();
        String getLocation();
        Integer getMinExperience();   // Must match JobPost.minExperience type (int -> Integer)
        Integer getMaxExperience();   // Must match JobPost.maxExperience type
    }
    interface CompanyInfo {
        Long getId();                 // Employer.id is Long
        String getCompanyName();
    }
}
