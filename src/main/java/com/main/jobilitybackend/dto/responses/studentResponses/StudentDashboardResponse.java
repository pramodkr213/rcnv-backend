package com.main.jobilitybackend.dto.responses.studentResponses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDashboardResponse {
	private String totalApplications;
    private String totalBookmarks;
    private int profileCompletion;
    private Long educationCount;
    
 //   private Long experienceCount;
 //   private Long skillCount;
}
