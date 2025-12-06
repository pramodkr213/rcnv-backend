package com.main.jobilitybackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipCardResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.JobCardResponse;
import com.main.jobilitybackend.entities.JobBookmarks;

import jakarta.transaction.Transactional;

public interface JobBookmarksRepo extends JpaRepository<JobBookmarks, Long> {

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
                    true AS isBookmark,
                    false AS isApplied
                FROM JobBookmarks jb
                JOIN jb.job jp
                WHERE jb.student.id = :studentId AND jb.deleteAt IS NULL AND jp.deleteAt IS NULL
            """)
    List<JobCardResponse> findBookmarkedJobsByStudentId(@Param("studentId") String studentId);

    boolean existsByJobIdAndStudentId(String jobId, String studentId);

    @Transactional
    void deleteByJobIdAndStudentId(String jobId, String studentId);

    boolean existsByInternshipIdAndStudentId(String internshipId, String studentId);

    @Transactional
    void deleteByInternshipIdAndStudentId(String internshipId, String studentId);

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
                    true AS isBookmark
                FROM JobBookmarks jb
                JOIN jb.internship i
                WHERE jb.student.id = :studentId AND jb.deleteAt IS NULL AND i.deleteAt IS NULL
            """)
    List<InternshipCardResponse> findBookmarkedInternshipsByStudentId(@Param("studentId") String studentId);
   
    Long countByStudentIdAndDeleteAtIsNull(String studentId);


}
