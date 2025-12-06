package com.main.jobilitybackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.dto.responses.application.ApplicationForAdmin;
import com.main.jobilitybackend.dto.responses.application.ApplicationResponse;
import com.main.jobilitybackend.dto.responses.application.IntershipApplications;
import com.main.jobilitybackend.dto.responses.application.JobApplications;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipCardResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.JobCardResponse;
import com.main.jobilitybackend.entities.JobApplication;

import jakarta.transaction.Transactional;

public interface JobApplicationRepo extends JpaRepository<JobApplication, Long> {

    long count();
    long countByInternship_IdIsNotNullAndJob_IdIsNull();
    long countByInternship_IdIsNullAndJob_IdIsNotNull();

    @Query("""
                SELECT
                    jp.id AS id,
                    jp.title AS title,
                    jp.description AS description,
                    jp.isFresher AS isFresher,
                    jp.minExperience AS minExperience,
                    jp.maxExperience AS maxExperience,
                    jp.location AS location,
                    jp.jobType AS jobType,
                    jp.mode As mode,
                    jp.skills AS skills,
                    jp.minSalary AS minSalary,
                    jp.maxSalary AS maxSalary,
                    jp.numOfWorkingDays AS numOfWorkingDays,
                    jp.otherSalaryDetails AS otherSalaryDetails,
                    jp.numberOfVacancies AS numberOfVacancies,
                    jp.postedBy AS postedBy,
                    jp.active AS active,
                    jp.createdAt AS createdAt,
                    jp.updatedAt AS updatedAt,
                    jp.employer.logoUrl AS logo,
                    jp.employer.companyName AS companyName,
                    false AS isBookmark,
                    true AS isApplied
                FROM JobApplication ja
                JOIN ja.job jp
                WHERE ja.applicant.id = :studentId AND ja.deleteAt IS NULL AND jp.deleteAt IS NULL
            """)
    List<JobCardResponse> findAppliedJobsByStudentId(@Param("studentId") String studentId);

    @Query("""
                SELECT
                    i.id AS id,
                    i.title AS title,
                    i.duration AS duration,
                    i.isPaid AS isPaid,
                    i.minStipend AS minStipend,
                    i.maxStipend AS maxStipend,
                    i.mode AS mode,
                    i.location AS location,
                    i.internshipType AS internshipType,
                    i.numberOfOpenings AS numberOfOpenings,
                    i.isImmediate AS isImmediate,
                    i.joinFrom AS joinFrom,
                    i.joinTo AS joinTo,
                    i.applicationDeadline AS applicationDeadline,
                    i.postedBy AS postedBy,
                    i.isActive AS isActive,
                    i.createdAt AS createdAt,
                    i.updatedAt AS updatedAt,
                    i.employer.logoUrl AS logo,
                    i.employer.companyName AS companyName,
                    false AS isBookmark,
                    true AS isApplied
                FROM JobApplication ja
                JOIN ja.internship i
                WHERE ja.applicant.id = :studentId AND ja.deleteAt IS NULL AND i.deleteAt IS NULL
            """)
    List<InternshipCardResponse> findAppliedInternshipsByStudentId(@Param("studentId") String studentId);

    @Query("""
                SELECT
                    a.appliedAt AS appliedAt,
                    a.id AS id,
                    a.status AS status,
                    a.applicant AS applicant,
                    a.job AS job,
                    a.internship AS internship,
                    j.employer AS jobEmployer,
                    i.employer AS internshipEmployer
                FROM JobApplication a
                LEFT JOIN a.job j
                LEFT JOIN a.internship i
                WHERE a.deleteAt IS NULL
                  AND (
                      :query IS NULL OR :query = '' OR
                      LOWER(CONCAT(a.applicant.firstName, ' ', a.applicant.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR
                      LOWER(a.applicant.email) LIKE LOWER(CONCAT('%', :query, '%')) OR
                      a.applicant.phone LIKE CONCAT('%', :query, '%')
                  )
                  AND (
                      :status IS NULL OR :status = '' OR a.status = :status
                  )
                  AND (
                      :companyName IS NULL OR :companyName = '' OR
                      LOWER(j.employer.companyName) LIKE LOWER(CONCAT('%', :companyName, '%')) OR
                      LOWER(i.employer.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))
                  )
                  AND (
                      :location IS NULL OR :location = '' OR
                      LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')) OR
                      LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))
                  )
                  AND (
                      :date IS NULL OR :date = '' OR
                      FUNCTION('DATE', a.appliedAt) = CAST(:date AS date)
                  )
                ORDER BY a.appliedAt DESC
            """)
    List<ApplicationForAdmin> findAllApplications(
            @Param("query") String query,
            @Param("date") String date,
            @Param("status") String status,
            @Param("companyName") String companyName,
            @Param("location") String location,
            Pageable pageable);

    @Query("""
                SELECT
                    a.appliedAt AS appliedAt,
                    a.id AS id,
                    a.status AS status,
                    a.applicant AS applicant,
                    a.internship AS internship,
                    i.employer AS company
                FROM JobApplication a
                LEFT JOIN a.internship i
                WHERE a.deleteAt IS NULL
                  AND (
                      :query IS NULL OR :query = '' OR
                      LOWER(CONCAT(a.applicant.firstName, ' ', a.applicant.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR
                      LOWER(a.applicant.email) LIKE LOWER(CONCAT('%', :query, '%')) OR
                      a.applicant.phone LIKE CONCAT('%', :query, '%')
                  )
                  AND (
                      :status IS NULL OR :status = '' OR a.status = :status
                  )
                  AND (
                      :companyName IS NULL OR :companyName = '' OR
                      LOWER(i.employer.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))
                  )
                  AND (
                      :location IS NULL OR :location = '' OR
                      LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))
                  )
                  AND (
                      :date IS NULL OR :date = '' OR
                      FUNCTION('DATE', a.appliedAt) = CAST(:date AS date)
                  )
                ORDER BY a.appliedAt DESC
            """)
    List<IntershipApplications> findAllInternshipApplications(
            @Param("query") String query,
            @Param("date") String date,
            @Param("status") String status,
            @Param("companyName") String companyName,
            @Param("location") String location,
            Pageable pageable);

    @Query("""
                SELECT
                    a.appliedAt AS appliedAt,
                    a.id AS id,
                    a.status AS status,
                    a.applicant AS applicant,
                    a.job AS job,
                    j.employer AS company
                FROM JobApplication a
                LEFT JOIN a.job j
                WHERE a.deleteAt IS NULL
                  AND (
                      :query IS NULL OR :query = '' OR
                      LOWER(CONCAT(a.applicant.firstName, ' ', a.applicant.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR
                      LOWER(a.applicant.email) LIKE LOWER(CONCAT('%', :query, '%')) OR
                      a.applicant.phone LIKE CONCAT('%', :query, '%')
                  )
                  AND (
                      :status IS NULL OR :status = '' OR a.status = :status
                  )
                  AND (
                      :companyName IS NULL OR :companyName = '' OR
                      LOWER(j.employer.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))
                  )
                  AND (
                      :location IS NULL OR :location = '' OR
                      LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))
                  )
                  AND (
                      :date IS NULL OR :date = '' OR
                      FUNCTION('DATE', a.appliedAt) = CAST(:date AS date)
                  )
                ORDER BY a.appliedAt DESC
            """)
    List<JobApplications> findAllJobApplications(
            @Param("query") String query,
            @Param("date") String date,
            @Param("status") String status,
            @Param("companyName") String companyName,
            @Param("location") String location,
            Pageable pageable);

    List<ApplicationResponse> findByJobIdAndDeleteAtIsNull(@Param("jobId") String jobId);

    List<ApplicationResponse> findByInternshipIdAndDeleteAtIsNull(@Param("internshipId") String intersnshipId);

    Optional<JobApplication> findByIdAndDeleteAtIsNull(Long id);
    boolean existsByInternship_IdAndApplicant_Id(@Param("internshipId") String internshipId, @Param("applicantId") String applicantId);
    boolean existsByJob_IdAndApplicant_Id(@Param("jobId") String jobId, @Param("applicantId") String applicantId);

    @Modifying
    @Transactional
    @Query("UPDATE JobApplication ja SET ja.deleteAt = CURRENT_TIMESTAMP WHERE ja.id = :id")
    void softDeleteApplicationById(@Param("id") Long id);

    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.applicant.id = :studentId AND ja.deleteAt IS NULL")
    long countByApplicant_Id(@Param("studentId") String studentId);
   
}
