package com.main.jobilitybackend.dto.requestDTO.JobRequestDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntershipPostRequest {
    private String title;
    private String duration;
    private boolean isPaid;
    private String minStipend;
    private String maxStipend;
    private String mode;
    private String location;
    private String intershipType;
private Boolean isActive;
    private String eligibility;
    private String skillsRequired;

    private int numberOfOpenings;
    private boolean isImmediate;
    private LocalDate joinFrom;
    private LocalDate joinTo;

    private LocalDate applicationDeadline;

    private String internshipDescription;

    private String postedBy;
}
