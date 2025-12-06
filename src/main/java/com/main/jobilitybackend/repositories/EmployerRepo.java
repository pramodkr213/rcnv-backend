package com.main.jobilitybackend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.dto.responses.employer.EmployerResponse;
import com.main.jobilitybackend.dto.responses.employer.EmployerDashboard;
import com.main.jobilitybackend.dto.responses.employer.EmplyerDetailResponse;
import com.main.jobilitybackend.entities.Employer;

import jakarta.transaction.Transactional;

public interface EmployerRepo extends JpaRepository<Employer, Long> {
    long count();

    Optional<Employer> findByEmailAndDeleteAtIsNull(@Param("email") String email);

    Optional<Employer> findByIdAndDeleteAtIsNull(@Param("id") Long id);

    boolean existsByEmailAndDeleteAtIsNull(String email);

    @Query("""
                SELECT new com.main.jobilitybackend.dto.responses.employer.EmployerDashboard(
                    COUNT(DISTINCT i.id),
                    COUNT(DISTINCT j.id),
                    COUNT(DISTINCT ja.id) + COUNT(DISTINCT ia.id)
                )
                FROM Employer e
                LEFT JOIN e.intershipPosts i
                LEFT JOIN e.jobPosts j
                LEFT JOIN i.applications ia
                LEFT JOIN j.jobApplications ja
                WHERE e.id = :employerId AND e.deleteAt IS NULL AND e.isEmailVerified = true AND e.verified = true
                GROUP BY e.id
            """)
    EmployerDashboard findEmployerDashboard(@Param("employerId") Long employerId);
    
    //totalCount for Dashboard
    @Query("""
    	    SELECT COUNT(DISTINCT j.id)
    	    FROM Employer e
    	    LEFT JOIN e.jobPosts j
    	    WHERE e.id = :employerId AND e.deleteAt IS NULL AND e.isEmailVerified = true AND e.verified = true
    	""")
    	Long countJobPosts(@Param("employerId") Long employerId);


    @Query("""
                SELECT
                    e.id AS id,
                    e.email AS email,
                    e.firstName AS firstName,
                    e.lastName AS lastName,
                    e.phone AS phone,
                    e.designation AS designation,
                    e.companyName AS companyName,
                    e.city AS city,
                    e.industryType AS industryType,
                    e.isEmailVerified AS isEmailVerified,
                    e.verified AS verified,
                    e.active As active,
                    e.createdAt AS createdAt,
                    0 AS jobPostCount,
                    0 AS internshipPostCount
                FROM Employer e
                WHERE e.deleteAt IS NULL
                  AND e.isEmailVerified = true
                  AND e.companyName IS NOT NULL
                  AND e.verified = false
                  AND (
                      :keyword IS NULL OR :keyword = '' OR
                      LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                      LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                      e.phone LIKE CONCAT('%', :keyword, '%')
                  )
                  AND (
                      :companyName IS NULL OR :companyName = '' OR LOWER(e.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))
                  )
                  AND (
                      :city IS NULL OR :city = '' OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%'))
                  )
                  AND (
                      :industryType IS NULL OR :industryType = '' OR LOWER(e.industryType) LIKE LOWER(CONCAT('%', :industryType, '%'))
                  )
            """)
    List<EmployerResponse> findPendingEmployersRequest(
            @Param("keyword") String keyword,
            @Param("companyName") String companyName,
            @Param("city") String city,
            @Param("industryType") String industryType,
            Pageable pageable);
    
    //TotalCount for Employer RequestAPI
    @Query("""
    	    SELECT COUNT(e)
    	    FROM Employer e
    	    WHERE e.deleteAt IS NULL
    	      AND e.isEmailVerified = true
    	      AND e.companyName IS NOT NULL
    	      AND e.verified = false
    	      AND (
    	          :keyword IS NULL OR :keyword = '' OR
    	          LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    	          LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    	          e.phone LIKE CONCAT('%', :keyword, '%')
    	      )
    	      AND (
    	          :companyName IS NULL OR :companyName = '' OR LOWER(e.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))
    	      )
    	      AND (
    	          :city IS NULL OR :city = '' OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%'))
    	      )
    	      AND (
    	          :industryType IS NULL OR :industryType = '' OR LOWER(e.industryType) LIKE LOWER(CONCAT('%', :industryType, '%'))
    	      )
    	""")
    	Long countPendingEmployersRequest(
    	        @Param("keyword") String keyword,
    	        @Param("companyName") String companyName,
    	        @Param("city") String city,
    	        @Param("industryType") String industryType);


    // all employers search 
    @Query(value = """
    SELECT
        e.id AS id,
        e.email AS email,
        e.first_name AS firstName,
        e.last_name AS lastName,
        e.phone AS phone,
        e.designation AS designation,
        e.company_name AS companyName,
        e.city AS city,
        e.industry_type AS industryType,
        e.is_email_verified AS isEmailVerified,
        e.verified AS verified,
        e.active AS active,
        e.created_at AS createdAt,
        COUNT(j.id) AS jobPostCount,
        COUNT(i.id) AS internshipPostCount
    FROM employer e
    LEFT JOIN job_post j ON e.id = j.employer_id
    LEFT JOIN internship_post i ON e.id = i.employer_id
    WHERE e.delete_at IS NULL
      AND e.is_email_verified = true
      AND e.verified = true
      or (:keyword IS NULL OR :keyword = '' OR
           LOWER(CONCAT(e.first_name, ' ', e.last_name)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           e.phone LIKE CONCAT('%', :keyword, '%'))
      AND (:companyName IS NULL OR :companyName = '' OR LOWER(e.company_name) LIKE LOWER(CONCAT('%', :companyName, '%')))
      AND (:city IS NULL OR :city = '' OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%')))
      AND (:industryType IS NULL OR :industryType = '' OR LOWER(e.industry_type) LIKE LOWER(CONCAT('%', :industryType, '%')))
      and (:dateStr IS NULL OR :dateStr = '' OR DATE(e.created_at) = TO_DATE(:dateStr, 'YYYY-MM-DD'))
    GROUP BY e.id
    """, nativeQuery = true)
List<Map<String,String>> searchEmployers(
        @Param("keyword") String keyword,
        @Param("companyName") String companyName,
        @Param("city") String city,
        @Param("industryType") String industryType,
        @Param("dateStr") String dateStr,
        Pageable pageable
);
  
    @Query(value = """
    	    SELECT COUNT(DISTINCT e.id)
    	    FROM employer e
    	    LEFT JOIN job_post j ON e.id = j.employer_id
    	    LEFT JOIN internship_post i ON e.id = i.employer_id
    	    WHERE e.delete_at IS NULL
    	      AND e.is_email_verified = true
    	      AND e.verified = true
    	      or (
    	           :keyword IS NULL OR :keyword = '' OR
    	           LOWER(CONCAT(e.first_name, ' ', e.last_name)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    	           LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    	           e.phone LIKE CONCAT('%', :keyword, '%')
    	      )
    	      AND (:companyName IS NULL OR :companyName = '' OR LOWER(e.company_name) LIKE LOWER(CONCAT('%', :companyName, '%')))
    	      AND (:city IS NULL OR :city = '' OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%')))
    	      AND (:industryType IS NULL OR :industryType = '' OR LOWER(e.industry_type) LIKE LOWER(CONCAT('%', :industryType, '%')))
    	      AND (:dateStr IS NULL OR :dateStr = '' OR DATE(e.created_at) = TO_DATE(:dateStr, 'YYYY-MM-DD'))
    	""", nativeQuery = true)
    	Long countFilteredEmployers(
    	        @Param("keyword") String keyword,
    	        @Param("companyName") String companyName,
    	        @Param("city") String city,
    	        @Param("industryType") String industryType,
    	        @Param("dateStr") String dateStr
    	);
 

    @Query("""
                SELECT
                    e.id AS id,
                    e.email AS email,
                    e.firstName AS firstName,
                    e.lastName AS lastName,
                    e.phone AS phone,
                    e.designation AS designation,
                    e.companyName AS companyName,
                    e.city AS city,
                    e.industryType AS industryType,
                    e.isEmailVerified AS isEmailVerified,
                    e.verified AS verified,
                    e.createdAt AS createdAt,
                    e.active As active,
                    COUNT(j.id) AS jobPostCount,
                    COUNT(i.id) AS internshipPostCoun,
                    e.discription AS discription,
                    e.noEmployees AS noEmployees,
                    e.logoUrl AS logoUrl,
                    e.documentUrl AS documentUrl,
                    e.website AS website,
                    e.smLink AS smLink,
                    e.isDocument AS isDocument
                FROM Employer e
                LEFT JOIN e.jobPosts j
                LEFT JOIN e.intershipPosts i
                WHERE e.id = :id AND e.deleteAt IS NULL AND e.isEmailVerified = true
                GROUP BY e.id, e.email, e.firstName, e.lastName, e.phone, e.designation,
                         e.companyName, e.city, e.industryType, e.isEmailVerified, e.verified,
                         e.createdAt, e.discription, e.noEmployees, e.logoUrl, e.documentUrl,
                         e.website, e.smLink, e.isDocument
            """)
    Optional<EmplyerDetailResponse> getEmployerDetail(@Param("id") Long id);

    @Query("""
                SELECT
                    COUNT(e.id)
                FROM Employer e
                WHERE e.deleteAt IS NULL
                  AND e.isEmailVerified = true
                  AND e.companyName IS NOT NULL
                  AND e.verified = false
            """)
    long countPendingRequests();

    @Modifying
    @Transactional
    @Query("UPDATE Employer e SET e.verified = :verified WHERE e.id = :id AND e.deleteAt IS NULL")
    void varifyEmployer(@Param("id") Long id, @Param("verified") boolean verified);

    @Modifying
    @Transactional
    @Query("UPDATE Employer e SET e.active = :active WHERE e.id = :id AND e.deleteAt IS NULL")
    void acitveAndInactiveEmployer(@Param("id") Long id, @Param("active") boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE Employer e SET e.deleteAt = CURRENT_TIMESTAMP WHERE e.id = :id ")
    void softDeleteEmployer(@Param("id") Long id);

}
