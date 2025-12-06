package com.main.jobilitybackend.dto.responses.internshipResponses;


public interface InternshipCardResponse extends InternshipResponse{
    String getCompanyName();
    String getLogo();
    boolean getIsBookmark();
    Boolean getIsApplied();
}
