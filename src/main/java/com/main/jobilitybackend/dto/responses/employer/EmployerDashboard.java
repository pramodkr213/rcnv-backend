package com.main.jobilitybackend.dto.responses.employer;

import lombok.Data;

@Data
public class EmployerDashboard {
    private Long totalInternships;
    private Long totalJobs;
    private Long totalApplications;

    public EmployerDashboard(Long totalInternships, Long totalJobs, Long totalApplications) {
        this.totalInternships = totalInternships;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
    }

    // Getters and setters (optional if using Lombok)
}