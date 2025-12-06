package com.main.jobilitybackend.dto.responses.jobReponses;

public interface JobDetailResponse extends EmployerJobs {
    String getDescription();
    String getOtherSalaryDetails();
    String getLastDate();
    boolean getIsBookmark();
    boolean getIsApplied();
    
    EmployerInfo getEmployer();

    interface EmployerInfo {
        String getCompanyName();
        String getEmail();
        String getPhone();
        String getDiscription();
    }
}
