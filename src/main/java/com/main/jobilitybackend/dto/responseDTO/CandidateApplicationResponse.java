package com.main.jobilitybackend.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateApplicationResponse {
    private Long id;
    private String title;
    private String companyName;
    private String location;
    private String skills;
    private Integer minSalary;
    private Integer maxSalary;
    private Integer stipend;
    private String type; // JOB or INTERNSHIP
}
