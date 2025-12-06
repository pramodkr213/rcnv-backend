package com.main.jobilitybackend.dto.responses.jobReponses;

import java.time.LocalDateTime;

public interface JobResponse {
    String getId();
    String getTitle();
    boolean getIsFresher();
    int getMinExperience();
    int getMaxExperience();
    String getLocation();
    String getJobType();
    String getMode();
    String getSkills();
    Long getMinSalary();
    Long getMaxSalary();
    int getNumOfWorkingDays();
    int getNumberOfVacancies();
    String getCompanyName();
    String getPostedBy();
    String getLastDate();
    boolean getActive();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
