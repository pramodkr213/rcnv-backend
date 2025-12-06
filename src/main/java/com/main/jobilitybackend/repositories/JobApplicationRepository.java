package com.main.jobilitybackend.repositories;

import com.main.jobilitybackend.entities.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Fetch active applications (only jobs)
    @Query("SELECT ja FROM JobApplication ja " +
           "WHERE ja.applicant.email = :email " +
           "AND ja.deleteAt IS NULL")
    List<JobApplication> findActiveApplicationsByEmail(@Param("email") String email);

    // Fetch both Jobs + Internships applied by student
    @Query(value = """
        SELECT j.id as id,
               j.title as title,
               j.company_name as companyName,
               j.location as location,
               j.skills as skills,
               j.min_salary as minSalary,
               j.max_salary as maxSalary,
               null as stipend,
               'JOB' as type
        FROM job_applications ja
        JOIN jobs j ON ja.job_id = j.id
        JOIN students s ON ja.applicant_id = s.id
        WHERE s.email = :email AND ja.delete_at IS NULL
        UNION
        SELECT i.id as id,
               i.title as title,
               i.company_name as companyName,
               i.location as location,
               i.skills as skills,
               null as minSalary,
               null as maxSalary,
               i.stipend as stipend,
               'INTERNSHIP' as type
        FROM job_applications ja
        JOIN internships i ON ja.internship_id = i.id
        JOIN students s ON ja.applicant_id = s.id
        WHERE s.email = :email AND ja.delete_at IS NULL
    """, nativeQuery = true)
    List<Map<String, Object>> findApplicationsByEmail(@Param("email") String email);
}
