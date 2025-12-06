package com.main.jobilitybackend.dto.responses.employer;

public interface EmplyerDetailResponse extends EmployerResponse{ 
    String getDiscription();
    String getNoEmployees();
    String getLogoUrl();
    String getDocumentUrl();
    String getWebsite();
    String getSmLink();
    boolean getIsDocument();
}
