package com.main.jobilitybackend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.dto.responses.studentResponses.StudentProfileResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponse;
import com.main.jobilitybackend.dto.responses.studentResponses.StudentResponseForAdmin;
import com.main.jobilitybackend.entities.Student;

import jakarta.transaction.Transactional;

public interface StudentRepo extends JpaRepository<Student, String> {

    Optional<StudentProfileResponse> findByEmailAndDeleteAtNull(String email);

    boolean existsByEmailAndDeleteAtIsNull(String email);
    
    Optional<Student> findByEmailAndDeleteAtIsNull(String email);

    Optional<Student> findByEmail(String email);

    Optional<Student> findById(String id);

    Optional<StudentProfileResponse> findByIdAndDeleteAtNull(String id);

    @Query("""
                SELECT
                    s.id AS id,
                    s.firstName AS firstName,
                    s.lastName AS lastName,
                    s.email AS email,
                    s.phone AS phone,
                    s.gender AS gender,
                    s.dob AS dob,
                    s.role AS role,
                    s.skills AS skills,
                    s.resumeLink AS resumeLink,
                    s.profilePicture AS profilePicture,
                    s.city AS city,
                    s.state AS state,
                    s.country AS country,
                    s.linkedinProfile AS linkedinProfile,
                    s.githubProfile AS githubProfile,
                    s.aboutMe AS aboutMe,
                    s.active AS active,
                    s.createdAt AS createdAt,
                    s.updatedAt AS updatedAt
                FROM Student s
                WHERE s.deleteAt IS NULL
                  AND (
                        :keyword IS NULL OR :keyword = '' OR
                        LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                        LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                        s.phone LIKE CONCAT('%', :keyword, '%')
                  )
                  AND (
                        :city IS NULL OR :city = '' OR LOWER(s.city) LIKE LOWER(CONCAT('%', :city, '%'))
                  )
                  AND (:date IS NULL OR DATE(s.createdAt) = DATE(:date))
                ORDER BY s.createdAt DESC
            """)
    List<StudentResponse> findAllByDeleteAtNullOrderByCreatedAtDesc(
            @Param("keyword") String keyword,
            @Param("city") String city,
            @Param("date") String date,
            Pageable pageable);

    @Query("""
                SELECT DISTINCT
                    s
                FROM Student s
                LEFT JOIN s.education e
                WHERE s.deleteAt IS NULL
                  AND (
                        :keyword IS NULL OR :keyword = '' OR
                        LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                        LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                        s.phone LIKE CONCAT('%', :keyword, '%')
                  )
                  AND (
                        :city IS NULL OR :city = '' OR LOWER(s.city) LIKE LOWER(CONCAT('%', :city, '%'))
                  )
                 AND (:date IS NULL OR :date = '' OR DATE(s.createdAt) = FUNCTION('DATE', FUNCTION('TO_DATE', :date, 'YYYY-MM-DD')))

                  AND (
                        :degree IS NULL OR :degree = '' OR LOWER(e.degree) LIKE LOWER(CONCAT('%', :degree, '%'))
                  )
                  AND (
                        :college IS NULL OR :college = '' OR LOWER(e.college) LIKE LOWER(CONCAT('%', :college, '%'))
                  )
                  AND (
                        :fieldOfStudy IS NULL OR :fieldOfStudy = '' OR LOWER(e.fieldOfStudy) LIKE LOWER(CONCAT('%', :fieldOfStudy, '%'))
                  )
                  AND (
                        :yearOfPassing IS NULL OR :yearOfPassing = '' OR e.yearOfPassing = :yearOfPassing
                  )
                ORDER BY s.createdAt DESC
            """)
    List<Student> findAllByFilters(
            @Param("keyword") String keyword,
            @Param("city") String city,
            @Param("date") String date,
            @Param("degree") String degree,
            @Param("college") String college,
            @Param("fieldOfStudy") String fieldOfStudy,
            @Param("yearOfPassing") String yearOfPassing,
            Pageable pageable);

    long count();

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.active = :active WHERE s.id = :id")
    void activeAndInactive(@Param("id") String id, @Param("active") boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.deleteAt = :CURRENT_TIMESTAMP WHERE s.id = :id")
    void softDeleteStudentById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.careerObjective = :careerObjective WHERE s.email = :email")
    void updateCareerObjectives(@Param("email")String email,@Param("careerObjective")String careerObject);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.aboutMe = :aboutMe WHERE s.email = :email")
    void updateAboutMe(@Param("email")String email,@Param("aboutMe")String aboutMe);

}