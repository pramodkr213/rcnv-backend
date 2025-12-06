package com.main.jobilitybackend.dto.responses.internshipResponses;


public interface InternshipDetailResponse extends EmployerIntership {
    String getInternshipDescription();
    String getEligibility();
    boolean getIsBookmark();
    Boolean getIsApplied();
    String getSkillsRequired();

    EmployerInfo getEmployer();

    interface EmployerInfo {
        String getCompanyName();
        String getEmail();
        String getPhone();
        String getDiscription();
    }
}
