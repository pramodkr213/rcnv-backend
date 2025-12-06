package com.main.jobilitybackend.dto.responses.adminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboard {
    long totalStudents;
    long totalEmployers;
    long totalJobs;
    long totalInternships;
    long totalInternshipApplications;
    long totalJobApplications;
    long totalPendingEmployerRequests;
}
