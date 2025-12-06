package com.main.jobilitybackend.dto.responses.studentResponses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileUpdate {
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private LocalDate dob;
    
    private String city;
    private String state;
    private String country;

    private String careerObjective;
    private String aboutMe;

    private String lanuagesKnown;
    
    private String linkedinProfile;
    private String githubProfile;
    private String portfolio;

}
