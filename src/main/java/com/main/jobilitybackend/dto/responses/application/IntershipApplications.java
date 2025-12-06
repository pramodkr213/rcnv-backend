package com.main.jobilitybackend.dto.responses.application;

public interface IntershipApplications extends ApplicationResponse{

    InternshipInfo getInternship();
    CompanyInfo getCompany();

    interface InternshipInfo {
        String getId();
        String getTitle();
        String getLocation();
        String getDuration();
        Boolean getIsPaid();         
        String getMinStipend();
        String getMaxStipend();
    }
    interface CompanyInfo {
        Long getId();              
        String getCompanyName();
    }
    
}
