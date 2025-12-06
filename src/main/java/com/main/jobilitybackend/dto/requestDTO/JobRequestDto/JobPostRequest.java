package com.main.jobilitybackend.dto.requestDTO.JobRequestDto;


import lombok.Data;

@Data
public class JobPostRequest {

    private String title; 
    private String description; 
    private Boolean active;
    private boolean isFresher;
    private int minExperience;
    private int maxExperience;
    
    private String location; 
    private String jobType;
    private String mode;

    private String skills;
    private long minSalary;
    private long maxSalary;
    private int numOfWorkingDays;
    private String otherSalaryDetails;

    private int numberOfVacancies;
    private String lastDate;

    private String postedBy;
    
    private String sector;
}
