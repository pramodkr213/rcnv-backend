package com.main.jobilitybackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.dto.responses.jobReponses.EmployerJobs;
import com.main.jobilitybackend.dto.responses.jobReponses.JobCardResponse;
import com.main.jobilitybackend.dto.responses.jobReponses.JobDetailResponse;
import com.main.jobilitybackend.entities.JobPost;

import jakarta.transaction.Transactional;

public interface JobPostRepo extends JpaRepository<JobPost, String> {
    long count();

//    @Query("""
//            SELECT
//                j.id AS id,
//                j.title AS title,
//                j.isFresher AS isFresher,
//                j.minExperience AS minExperience,
//                j.maxExperience AS maxExperience,
//                j.location AS location,
//                j.jobType AS jobType,
//                j.mode AS mode,
//                j.skills AS skills,
//                j.minSalary AS minSalary,
//                j.maxSalary AS maxSalary,
//                j.numOfWorkingDays AS numOfWorkingDays,
//                j.numberOfVacancies AS numberOfVacancies,
//                j.postedBy AS postedBy,
//                j.active AS active,
//                j.createdAt AS createdAt,
//                j.updatedAt AS updatedAt,
//                SIZE(j.jobApplications) AS numberOfApplications
//            FROM JobPost j
//            WHERE j.employer.id = :employerId AND j.deleteAt IS NULL
//            AND (:location IS NULL OR j.location = :location)
//             AND (:title IS NULL OR LOWER(CAST(j.title AS string)) LIKE LOWER(CONCAT('%', :title, '%')))
//            """)
//    List<EmployerJobs> findByEmployerIdAndDeleteAtIsNull(Long employerId,
//     @Param("location") String location,
//     @Param("title") String title);
    
    @Query(value = """
    	    SELECT
    	        j.id AS id,
    	        j.title AS title,
    	        j.is_fresher AS isFresher,
    	        j.min_experience AS minExperience,
    	        j.max_experience AS maxExperience,
    	        j.location AS location,
    	        j.job_type AS jobType,
    	        j.mode AS mode,
    	        j.skills AS skills,
    	        j.min_salary AS minSalary,
    	        j.max_salary AS maxSalary,
    	        j.num_of_working_days AS numOfWorkingDays,
    	        j.number_of_vacancies AS numberOfVacancies,
    	        j.posted_by AS postedBy,
    	        j.active AS active,
    	        j.created_at AS createdAt,
    	        j.updated_at AS updatedAt,
    	        (
    	            SELECT COUNT(*)
    	            FROM job_application ja
    	            WHERE ja.job_post_id = j.id
    	        ) AS numberOfApplications
    	    FROM job_post j
    	    WHERE j.employer_id = :employerId
    	      AND j.delete_at IS NULL
    	      AND (:location IS NULL OR j.location = :location)
    	      AND (:title IS NULL OR LOWER(CAST(j.title AS TEXT)) LIKE LOWER(CONCAT('%', :title, '%')))
    	""", nativeQuery = true)
    	List<EmployerJobs> findByEmployerIdAndDeleteAtIsNull(
    	    @Param("employerId") Long employerId,
    	    @Param("location") String location,
    	    @Param("title") String title
    	);
    
    //totalCount for employer jobpost
    @Query(value = """
    	    SELECT COUNT(*) 
    	    FROM job_post j 
    	    WHERE j.employer_id = :employerId 
    	      AND j.delete_at IS NULL 
    	      AND (:location IS NULL OR j.location = :location) 
    	      AND (:title IS NULL OR LOWER(CAST(j.title AS TEXT)) LIKE LOWER(CONCAT('%', :title, '%')))
    	    """, nativeQuery = true)
    	Long countByEmployerWithFiltersNative(
    	    @Param("employerId") Long employerId,
    	    @Param("location") String location,
    	    @Param("title") String title
    	);



    @Query("""
                SELECT
                    j.id AS id,
                    j.title AS title,
                    j.isFresher AS isFresher,
                    j.minExperience AS minExperience,
                    j.maxExperience AS maxExperience,
                    j.location AS location,
                    j.jobType AS jobType,
                    j.mode AS mode,
                    j.skills AS skills,
                    j.minSalary AS minSalary,
                    j.maxSalary AS maxSalary,
                    j.numOfWorkingDays AS numOfWorkingDays,
                    j.numberOfVacancies AS numberOfVacancies,
                    j.postedBy AS postedBy,
                    j.active AS active,
                    j.createdAt AS createdAt,
                    j.updatedAt AS updatedAt,
                    j.description AS description,
                    j.otherSalaryDetails AS otherSalaryDetails,
                    j.lastDate As lastDate,
                    CASE
                    WHEN :studentId IS NOT NULL AND b.id IS NOT NULL THEN true
                    ELSE false
                END AS isBookmark,
                CASE
                    WHEN :studentId IS NOT NULL AND a.id IS NOT NULL THEN true
                    ELSE false
                END AS isApplied,
                    SIZE(j.jobApplications) AS numberOfApplications,

                    j.employer As employer
                FROM JobPost j
                LEFT JOIN JobBookmarks b ON b.job.id = j.id AND b.deleteAt IS NULL AND b.student.id = :studentId
                LEFT JOIN JobApplication a ON a.job.id = j.id AND a.deleteAt IS NULL AND a.applicant.id = :studentId
                WHERE j.id = :id AND j.deleteAt IS NULL
            """)
    Optional<JobDetailResponse> findByIdAndDeleteAtIsNull(@Param("id") String id, @Param("studentId") String studentId);

//    @Query("""
//            SELECT
//                j.id AS id,
//                j.title AS title,
//                j.isFresher AS isFresher,
//                j.minExperience AS minExperience,
//                j.maxExperience AS maxExperience,
//                j.location AS location,
//                j.jobType AS jobType,
//                j.mode AS mode,
//                j.skills AS skills,
//                j.minSalary AS minSalary,
//                j.maxSalary AS maxSalary,
//                j.numOfWorkingDays AS numOfWorkingDays,
//                j.numberOfVacancies AS numberOfVacancies,
//                j.postedBy AS postedBy,
//                j.active AS active,
//                j.createdAt AS createdAt,
//                j.updatedAt AS updatedAt,
//                j.employer.logoUrl AS logo,
//                j.employer.companyName AS companyName,
//                j.employer.industryType AS industryType,
//                CASE
//                    WHEN :studentId IS NOT NULL AND b.id IS NOT NULL THEN true
//                    ELSE false
//                END AS isBookmark,
//                CASE
//                    WHEN :studentId IS NOT NULL AND a.id IS NOT NULL THEN true
//                    ELSE false
//                END AS isApplied
//            FROM JobPost j
//            LEFT JOIN JobBookmarks b ON b.job.id = j.id AND b.deleteAt IS NULL AND b.student.id = :studentId
//            LEFT JOIN JobApplication a ON a.job.id = j.id AND a.deleteAt IS NULL AND a.applicant.id = :studentId
//            WHERE j.deleteAt IS NULL
//              AND (:title IS NULL OR LOWER(j.title) LIKE CONCAT('%', LOWER(:title),'%'))
//              AND (:location IS NULL OR j.location = :location)
//              AND (:jobType IS NULL OR j.jobType = :jobType)
//              AND (:mode IS NULL OR j.mode = :mode)
//              AND (:minExperience IS NULL OR j.minExperience >= :minExperience)
//              AND (:maxExperience IS NULL OR j.maxExperience <= :maxExperience)
//              AND (:isFresher IS NULL OR j.isFresher = :isFresher)
//              AND (:minSalary IS NULL OR j.minSalary >= :minSalary)
//              AND (:maxSalary IS NULL OR j.maxSalary <= :maxSalary)
//            """)
//    List<JobCardResponse> findFilteredJobPosts(
//            @Param("studentId") String studentId,
//            @Param("title") String title,
//            @Param("location") String location,
//            @Param("jobType") String jobType,
//            @Param("mode") String mode,
//            @Param("minExperience") Integer minExperience,
//            @Param("maxExperience") Integer maxExperience,
//            @Param("isFresher") Boolean isFresher,
//            @Param("minSalary") Long minSalary,
//            @Param("maxSalary") Long maxSalary,
//            Pageable pageable);
    
    @Query(value = """
    	    SELECT 
    	        jp.id AS id,
    	        jp.title AS title,
    	        jp.is_fresher AS isFresher,
    	        jp.min_experience AS minExperience,
    	        jp.max_experience AS maxExperience,
    	        jp.location AS location,
    	        jp.job_type AS jobType,
    	        jp.mode AS mode,
    	        jp.skills AS skills,
    	        jp.min_salary AS minSalary,
    	        jp.max_salary AS maxSalary,
    	        jp.num_of_working_days AS numOfWorkingDays,
    	        jp.number_of_vacancies AS numberOfVacancies,
    	        jp.posted_by AS postedBy,
    	        jp.active AS active,
    	        jp.created_at AS createdAt,
    	        jp.updated_at AS updatedAt,
    	        e.logo_url AS logo,
    	        e.company_name AS companyName,
    	        e.industry_type AS industryType,
    	        CASE WHEN :studentId IS NOT NULL AND jb.id IS NOT NULL THEN true ELSE false END AS isBookmark,
    	        CASE WHEN :studentId IS NOT NULL AND ja.id IS NOT NULL THEN true ELSE false END AS isApplied
    	    FROM job_post jp
    	    JOIN employer e ON e.id = jp.employer_id
    	    LEFT JOIN job_bookmarks jb ON jb.job_post_id = jp.id AND jb.delete_at IS NULL AND jb.student_id = :studentId
    	    LEFT JOIN job_application ja ON ja.job_post_id = jp.id AND ja.delete_at IS NULL AND ja.student_id = :studentId
    	    WHERE jp.delete_at IS NULL
    	      AND (:title IS NULL OR LOWER(jp.title) LIKE LOWER(CONCAT('%', :title, '%')))
    	      AND (:sector IS NULL OR LOWER(jp.sector) LIKE LOWER(CONCAT('%', :sector, '%')))
    	      AND (:location IS NULL OR jp.location = :location)
    	      AND (:jobType IS NULL OR jp.job_type = :jobType)
    	      AND (:mode IS NULL OR jp.mode = :mode)
    	      AND (:minExperience IS NULL OR jp.min_experience >= :minExperience)
    	      AND (:maxExperience IS NULL OR jp.max_experience <= :maxExperience)
    	      AND (:isFresher IS NULL OR jp.is_fresher = :isFresher)
    	      AND (:minSalary IS NULL OR jp.min_salary >= :minSalary)
    	      AND (:maxSalary IS NULL OR jp.max_salary <= :maxSalary)
    	    ORDER BY jp.created_at DESC
    	    LIMIT :limit OFFSET :offset
    	    """,
    	    nativeQuery = true)
    	List<JobCardResponse> findFilteredJobPostsNative(
    	        @Param("studentId") String studentId,
    	        @Param("title") String title,
    	        @Param("location") String location,
    	        @Param("jobType") String jobType,
    	        @Param("mode") String mode,
    	        @Param("minExperience") Integer minExperience,
    	        @Param("maxExperience") Integer maxExperience,
    	        @Param("isFresher") Boolean isFresher,
    	        @Param("minSalary") Long minSalary,
    	        @Param("maxSalary") Long maxSalary,
    	        @Param("limit") int limit,
    	        @Param("offset") int offset,
    	        @Param("sector") String sector);

    //calculate total count
    @Query(value = """
    	    SELECT COUNT(*)
    	    FROM job_post jp
    	    JOIN employer e ON e.id = jp.employer_id
    	    LEFT JOIN job_bookmarks jb ON jb.job_post_id = jp.id AND jb.delete_at IS NULL AND jb.student_id = :studentId
    	    LEFT JOIN job_application ja ON ja.job_post_id = jp.id AND ja.delete_at IS NULL AND ja.student_id = :studentId
    	    WHERE jp.delete_at IS NULL
    	      AND (:title IS NULL OR LOWER(jp.title) LIKE LOWER(CONCAT('%', :title, '%')))
    	      AND (:sector IS NULL OR LOWER(jp.sector) LIKE LOWER(CONCAT('%', :sector, '%')))
    	      AND (:location IS NULL OR jp.location = :location)
    	      AND (:jobType IS NULL OR jp.job_type = :jobType)
    	      AND (:mode IS NULL OR jp.mode = :mode)
    	      AND (:minExperience IS NULL OR jp.min_experience >= :minExperience)
    	      AND (:maxExperience IS NULL OR jp.max_experience <= :maxExperience)
    	      AND (:isFresher IS NULL OR jp.is_fresher = :isFresher)
    	      AND (:minSalary IS NULL OR jp.min_salary >= :minSalary)
    	      AND (:maxSalary IS NULL OR jp.max_salary <= :maxSalary)
    	    """, nativeQuery = true)
    	long countFilteredJobPostsNative(
    	        @Param("studentId") String studentId,
    	        @Param("title") String title,
    	        @Param("location") String location,
    	        @Param("jobType") String jobType,
    	        @Param("mode") String mode,
    	        @Param("minExperience") Integer minExperience,
    	        @Param("maxExperience") Integer maxExperience,
    	        @Param("isFresher") Boolean isFresher,
    	        @Param("minSalary") Long minSalary,
    	        @Param("maxSalary") Long maxSalary,
    	        @Param("sector") String sector);


    // @Query("""
    //             SELECT
    //                 j.id AS id,
    //                 j.title AS title,
    //                 j.isFresher AS isFresher,
    //                 j.minExperience AS minExperience,
    //                 j.maxExperience AS maxExperience,
    //                 j.location AS location,
    //                 j.jobType AS jobType,
    //                 j.mode AS mode,
    //                 j.skills AS skills,
    //                 j.minSalary AS minSalary,
    //                 j.maxSalary AS maxSalary,
    //                 j.numOfWorkingDays AS numOfWorkingDays,
    //                 j.numberOfVacancies AS numberOfVacancies,
    //                 j.postedBy AS postedBy,
    //                 j.active AS active,
    //                 j.createdAt AS createdAt,
    //                 j.updatedAt AS updatedAt,
    //                 j.employer.logoUrl AS logo,
    //                 j.employer.companyName AS companyName,
    //                 j.employer.industryType AS industryType,
    //                 false AS isBookmark,
    //                 false AS isApplied
    //             FROM JobPost j
    //             WHERE j.deleteAt IS NULL
    //               AND (:title IS NULL OR LOWER(CAST(j.title AS text)) LIKE LOWER(CONCAT('%', :title, '%')))
    //               AND (:location IS NULL OR j.location = :location)
    //               AND (:companyName IS NULL OR LOWER(CAST(j.employer.companyName AS text)) LIKE LOWER(CONCAT('%', :companyName, '%')))
    //               AND (:date IS NULL OR DATE(j.createdAt) = DATE(:date))
    //         """)
    // List<JobDetailResponse> findFilteredJobPosts(
    //         @Param("title") String title,
    //         @Param("location") String location,
    //         @Param("companyName") String companyName,
    //         @Param("date") String date,
    //         Pageable pageable);

    @Query(
  value = """
    SELECT
      jp.id             AS id,
      jp.title          AS title,
      jp.is_fresher     AS isFresher,
      jp.min_experience AS minExperience,
      jp.max_experience AS maxExperience,
      jp.location       AS location,
      jp.job_type       AS jobType,
      jp.mode           AS mode,
      jp.skills         AS skills,
      jp.min_salary     AS minSalary,
      jp.max_salary     AS maxSalary,
      jp.num_of_working_days    AS numOfWorkingDays,
      jp.number_of_vacancies    AS numberOfVacancies,
      jp.posted_by      AS postedBy,
      jp.active         AS active,
      jp.last_date as lastDate,
      jp.created_at     AS createdAt,
      jp.updated_at     AS updatedAt,
      e.logo_url        AS logoUrl,
      e.company_name    AS companyName,
      e.industry_type   AS industryType,
      false             AS isBookmark,
      false             AS isApplied
    FROM job_post jp
    JOIN employer  e  ON e.id = jp.employer_id
    WHERE jp.delete_at IS NULL
      AND (:title       IS NULL OR LOWER(jp.title)        LIKE CONCAT('%', LOWER(:title),       '%'))
      AND (:location    IS NULL OR jp.location = :location)
      AND (:companyName IS NULL OR LOWER(e.company_name) LIKE CONCAT('%', LOWER(:companyName), '%'))
      AND (:date        IS NULL OR DATE(jp.created_at) = DATE(:date))
    """,
  nativeQuery = true
)
List<JobDetailResponse> findFilteredJobPosts(
    @Param("title")       String title,
    @Param("location")    String location,
    @Param("companyName") String companyName,
    @Param("date")        String date,
    Pageable pageable
);
    
    @Query(value = """
    	    SELECT COUNT(*)
    	    FROM job_post jp
    	    JOIN employer e ON e.id = jp.employer_id
    	    WHERE jp.delete_at IS NULL
    	      AND (:title       IS NULL OR LOWER(jp.title)        LIKE CONCAT('%', LOWER(:title),       '%'))
    	      AND (:location    IS NULL OR jp.location = :location)
    	      AND (:companyName IS NULL OR LOWER(e.company_name) LIKE CONCAT('%', LOWER(:companyName), '%'))
    	      AND (:date        IS NULL OR DATE(jp.created_at) = DATE(:date))
    	    """,
    	  nativeQuery = true
    	)
    	Long countFilteredJobPosts(
    	    @Param("title")       String title,
    	    @Param("location")    String location,
    	    @Param("companyName") String companyName,
    	    @Param("date")        String date
    	);





    @Modifying
    @Transactional
    @Query("UPDATE JobPost j SET j.active = :active WHERE j.id = :id")
    void activeAndDeactivateJobPostById(String id, boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE JobPost j SET j.deleteAt = CURRENT_TIMESTAMP WHERE j.id = :id")
    void softDeleteJobPostById(String id);

    @Query("""
            SELECT
                j.id AS id,
                j.title AS title,
                j.isFresher AS isFresher,
                j.minExperience AS minExperience,
                j.maxExperience AS maxExperience,
                j.location AS location,
                j.jobType AS jobType,
                j.mode AS mode,
                j.skills AS skills,
                j.minSalary AS minSalary,
                j.maxSalary AS maxSalary,
                j.numOfWorkingDays AS numOfWorkingDays,
                j.numberOfVacancies AS numberOfVacancies,
                j.postedBy AS postedBy,
                j.active AS active,
                j.createdAt AS createdAt,
                j.updatedAt AS updatedAt,
                j.employer.logoUrl AS logo,
                j.employer.companyName AS companyName,
                j.employer.industryType AS industryType,
                false AS isBookmark,
                false AS isApplied
            FROM JobPost j
            WHERE j.deleteAt IS NULL
            ORDER BY j.createdAt DESC
            """)
    List<JobCardResponse> findTop5MostRecentJobs(Pageable pageable);

}
