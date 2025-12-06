package com.main.jobilitybackend.dto.responses.jobReponses;


public interface JobCardResponse extends JobResponse {
   
    String getPostedBy();
    boolean getActive();
    boolean getIsBookmark();
    boolean getIsApplied();
    String getLogo();
    String getCompanyName();
    String getIndustryType();
}
