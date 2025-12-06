package com.main.jobilitybackend.dto.responses.studentResponses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface StudentResponse {

    String getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPhone();

    String getGender();

    LocalDate getDob();

    String getRole();

    String getLanuagesKnown();

    String getSkills();

    String getResumeLink();

    String getProfilePicture();

    String getCity();

    String getState();

    String getCountry();

    String getLinkedinProfile();

    String getGithubProfile();

    String getPortfolio();

    String getCareerObjective();

    String getAboutMe();

    boolean getActive();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    
}
