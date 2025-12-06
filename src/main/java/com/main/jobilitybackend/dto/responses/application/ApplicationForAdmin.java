package com.main.jobilitybackend.dto.responses.application;

import java.time.LocalDateTime;

public interface ApplicationForAdmin {
    LocalDateTime getAppliedAt();
    Long getId();
    String getStatus();
    StudentInfo getApplicant();
    JobInfo getJob();
    InternshipInfo getInternship();
    CompanyInfo getCompany();

    interface JobInfo {
        String getId();
        String getTitle();
        String getLocation();
        Integer getMinExperience();   // Must match JobPost.minExperience type (int -> Integer)
        Integer getMaxExperience();   // Must match JobPost.maxExperience type
    }
    interface InternshipInfo {
        String getId();
        String getTitle();
        String getLocation();
        String getDuration();
        Boolean getIsPaid();          // boolean -> Boolean wrapper type
        String getMinStipend();
        String getMaxStipend();
    }
    interface CompanyInfo {
        Long getId();                 // Employer.id is Long
        String getCompanyName();
    }
    interface StudentInfo {
        String getId();
        String getFirstName();
        String getLastName();
        String getEmail();
        String getPhone();
        String getCity();
        String getResumeLink();
    }
}
