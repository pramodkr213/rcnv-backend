package com.main.jobilitybackend.dto.requestDTO;

import lombok.Data;

@Data
public class StudentProfileRequest {

    private String dob;

    private String city;

    private String state;

    private String country;

    private String linkedinProfile;

    private String githubProfile;

    private String aboutMe;
}
