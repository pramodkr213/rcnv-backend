package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.jobilitybackend.entities.JobApplication;

public interface ApplicationRepo extends JpaRepository<JobApplication, Long> {
    Long countByApplicantId(String studentId); // mappedBy applicant
}
