package com.main.jobilitybackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.dto.responses.internshipResponses.EmployerIntership;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipCardResponse;
import com.main.jobilitybackend.dto.responses.internshipResponses.InternshipDetailResponse;
import com.main.jobilitybackend.entities.InternshipPost;

import jakarta.transaction.Transactional;

public interface InternshipRepo extends JpaRepository<InternshipPost, String> {
    
    long count();

    Optional<InternshipPost> findByIdAndDeleteAtIsNull(String id);

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
                    SIZE(i.applications) AS numberOfApplications
                FROM InternshipPost i
                WHERE i.employer.id = :employerId AND i.deleteAt IS NULL
            """)
    List<EmployerIntership> findByEmployerIdAndDeleteAtIsNull(Long employerId);
    
    @Query(value = """
    	    SELECT COUNT(*) 
    	    FROM internship_post i 
    	    WHERE i.employer_id = :employerId 
    	      AND i.delete_at IS NULL
    	    """, nativeQuery = true)
    	Long countInternshipsByEmployerIdAndDeleteAtIsNull(@Param("employerId") Long employerId);


//    @Query("""
//                SELECT
//                    i.id AS id,
//                    i.title AS title,
//                    i.duration AS duration,
//                    i.isPaid AS isPaid,
//                    i.minStipend AS minStipend,
//                    i.maxStipend AS maxStipend,
//                    i.mode AS mode,
//                    i.location AS location,
//                    i.internshipType AS internshipType,
//                    i.numberOfOpenings AS numberOfOpenings,
//                    i.isImmediate AS isImmediate,
//                    i.joinFrom AS joinFrom,
//                    i.joinTo AS joinTo,
//                    i.applicationDeadline AS applicationDeadline,
//                    i.postedBy AS postedBy,
//                    i.isActive AS isActive,
//                    i.createdAt AS createdAt,
//                    i.updatedAt AS updatedAt,
//                    i.deleteAt AS deleteAt,
//                    i.employer.companyName AS companyName,
//                    i.employer.logoUrl AS logo,
//                    CASE
//                        WHEN :studentId IS NOT NULL AND b.id IS NOT NULL THEN true
//                        ELSE false
//                    END AS isBookmark,
//                    CASE
//                        WHEN :studentId IS NOT NULL AND a.id IS NOT NULL THEN true
//                        ELSE false
//                    END AS isApplied
//                FROM InternshipPost i
//                LEFT JOIN JobBookmarks b ON b.internship.id = i.id AND b.deleteAt IS NULL AND b.student.id = :studentId
//                LEFT JOIN JobApplication a ON a.internship.id = i.id AND a.deleteAt IS NULL AND a.applicant.id = :studentId
//                WHERE i.deleteAt IS NULL
//                  AND i.isActive = true
//                  AND (:title IS NULL OR LOWER(CAST(i.title AS string)) LIKE LOWER(CONCAT('%', :title, '%')))
//                  AND (:location IS NULL OR i.location = :location)
//                  AND (:intershipType IS NULL OR i.internshipType = :intershipType)
//                  AND (:mode IS NULL OR i.mode = :mode)
//                  AND (:minStipend IS NULL OR CAST(i.minStipend AS long) >= :minStipend)
//                  AND (:maxStipend IS NULL OR CAST(i.maxStipend AS long) <= :maxStipend)
//                  AND (:isPaid IS NULL OR i.isPaid = :isPaid)
//            """)
//    List<InternshipCardResponse> findFilteredInternshipPosts(
//            @Param("studentId") String studentId,
//            @Param("title") String title,
//            @Param("location") String location,
//            @Param("intershipType") String intershipType,
//            @Param("minStipend") Long minStipend,
//            @Param("maxStipend") Long maxStipend,
//            @Param("isPaid") Boolean isPaid,
//            @Param("mode") String mode,
//            Pageable pageable);
    
    @Query(value = """
    	    SELECT
    	        i.id AS id,
    	        i.title AS title,
    	        i.duration AS duration,
    	        i.is_paid AS isPaid,
    	        i.min_stipend AS minStipend,
    	        i.max_stipend AS maxStipend,
    	        i.mode AS mode,
    	        i.location AS location,
    	        i.internship_type AS internshipType,
    	        i.number_of_openings AS numberOfOpenings,
    	        i.is_immediate AS isImmediate,
    	        i.join_from AS joinFrom,
    	        i.join_to AS joinTo,
    	        i.application_deadline AS applicationDeadline,
    	        i.posted_by AS postedBy,
    	        i.is_active AS isActive,
    	        i.created_at AS createdAt,
    	        i.updated_at AS updatedAt,
    	        i.delete_at AS deleteAt,
    	        e.company_name AS companyName,
    	        e.logo_url AS logo,

    	        -- Bookmark flag
    	        CASE
    	            WHEN :studentId IS NOT NULL AND jb.id IS NOT NULL THEN TRUE
    	            ELSE FALSE
    	        END AS isBookmark,

    	        -- Applied flag
    	        CASE
    	            WHEN :studentId IS NOT NULL AND ja.id IS NOT NULL THEN TRUE
    	            ELSE FALSE
    	        END AS isApplied

    	    FROM internship_post i

    	    JOIN employer e ON e.id = i.employer_id

    	    LEFT JOIN job_bookmarks jb ON jb.internship_post_id = i.id AND jb.delete_at IS NULL AND jb.student_id = :studentId
    	    LEFT JOIN job_application ja ON ja.internship_post_id = i.id AND ja.delete_at IS NULL AND ja.student_id = :studentId

    	    WHERE i.delete_at IS NULL
    	      AND i.is_active = TRUE

    	      AND (:title IS NULL OR LOWER(CAST(i.title AS TEXT)) LIKE LOWER(CONCAT('%', :title, '%')))
    	      AND (:location IS NULL OR i.location = :location)
    	      AND (:intershipType IS NULL OR i.internship_type = :intershipType)
    	      AND (:mode IS NULL OR i.mode = :mode)
    	      AND (:minStipend IS NULL OR CAST(i.min_stipend AS BIGINT) >= :minStipend)
    	      AND (:maxStipend IS NULL OR CAST(i.max_stipend AS BIGINT) <= :maxStipend)
    	      AND (:isPaid IS NULL OR i.is_paid = :isPaid)

    	    ORDER BY i.created_at DESC
    	    LIMIT :limit OFFSET :offset
    	""", nativeQuery = true)
    	List<InternshipCardResponse> findFilteredInternshipPostsNative(
    	    @Param("studentId") String studentId,
    	    @Param("title") String title,
    	    @Param("location") String location,
    	    @Param("intershipType") String intershipType,
    	    @Param("minStipend") Long minStipend,
    	    @Param("maxStipend") Long maxStipend,
    	    @Param("isPaid") Boolean isPaid,
    	    @Param("mode") String mode,
    	    @Param("limit") int limit,
    	    @Param("offset") int offset
    	);



    //count for internship 
    @Query(value = """
    	    SELECT COUNT(*)
    	    FROM internship_post i
    	    JOIN employer e ON e.id = i.employer_id
    	    LEFT JOIN job_bookmarks jb 
    	        ON jb.internship_post_id = i.id 
    	        AND jb.delete_at IS NULL 
    	        AND (:studentId IS NOT NULL AND jb.student_id = :studentId)
    	    LEFT JOIN job_application ja 
    	        ON ja.internship_post_id = i.id 
    	        AND ja.delete_at IS NULL 
    	        AND (:studentId IS NOT NULL AND ja.student_id = :studentId)
    	    WHERE i.delete_at IS NULL
    	      AND i.is_active = TRUE
    	      AND (:title IS NULL OR LOWER(CAST(i.title AS TEXT)) LIKE LOWER(CONCAT('%', :title, '%')))
    	      AND (:location IS NULL OR i.location = :location)
    	      AND (:intershipType IS NULL OR i.internship_type = :intershipType)
    	      AND (:mode IS NULL OR i.mode = :mode)
    	      AND (:minStipend IS NULL OR CAST(i.min_stipend AS BIGINT) >= :minStipend)
    	      AND (:maxStipend IS NULL OR CAST(i.max_stipend AS BIGINT) <= :maxStipend)
    	      AND (:isPaid IS NULL OR i.is_paid = :isPaid)
    	    """, nativeQuery = true)
    	Long countFilteredInternshipPostsNative(
    	    @Param("studentId") String studentId,
    	    @Param("title") String title,
    	    @Param("location") String location,
    	    @Param("intershipType") String intershipType,
    	    @Param("minStipend") Long minStipend,
    	    @Param("maxStipend") Long maxStipend,
    	    @Param("isPaid") Boolean isPaid,
    	    @Param("mode") String mode
    	);




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
                    i.deleteAt AS deleteAt,
                    i.internshipDescription AS internshipDescription,
                    i.eligibility AS eligibility,
                    i.skillsRequired AS skillsRequired,
                    CASE
                    WHEN :studentId IS NOT NULL AND b.id IS NOT NULL THEN true
                    ELSE false
                END AS isBookmark,
                CASE
                    WHEN :studentId IS NOT NULL AND a.id IS NOT NULL THEN true
                    ELSE false
                END AS isApplied,
                    SIZE(i.applications) AS numberOfApplications,

                    i.employer As employer
                FROM InternshipPost i
                LEFT JOIN JobBookmarks b ON b.internship.id = i.id AND b.deleteAt IS NULL AND b.student.id = :studentId
                LEFT JOIN JobApplication a ON a.internship.id = i.id AND a.deleteAt IS NULL AND a.applicant.id = :studentId
                WHERE i.id = :id AND i.deleteAt IS NULL AND i.isActive = true
            """)
    Optional<InternshipDetailResponse> findByIdAndDeleteAtIsNullAndIsActiveTrue(@Param("id") String id,
                                                          @Param("studentId") String studentId);

    // @Query("""
    //             SELECT
    //                 i.id AS id,
    //                 i.title AS title,
    //                 i.duration AS duration,
    //                 i.isPaid AS isPaid,
    //                 i.minStipend AS minStipend,
    //                 i.maxStipend AS maxStipend,
    //                 i.mode AS mode,
    //                 i.location AS location,
    //                 i.internshipType AS internshipType,
    //                 i.numberOfOpenings AS numberOfOpenings,
    //                 i.isImmediate AS isImmediate,
    //                 i.joinFrom AS joinFrom,
    //                 i.joinTo AS joinTo,
    //                 i.applicationDeadline AS applicationDeadline,
    //                 i.postedBy AS postedBy,
    //                 i.isActive AS isActive,
    //                 i.createdAt AS createdAt,
    //                 i.updatedAt AS updatedAt,
    //                 false AS isBookmark,
    //                 false AS isApplied
    //             FROM InternshipPost i
    //             WHERE (:companyName IS NULL OR LOWER(i.employer.companyName) LIKE LOWER(CONCAT('%', :companyName, '%')))
    //               AND (:location IS NULL OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%')))
    //               AND (:title IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :title, '%')))
    //               AND (:date IS NULL OR DATE(i.createdAt) = DATE(:date))
    //               AND i.deleteAt IS NULL
    //         """)


            @Query(value = """
    SELECT
      i.id,
      i.title,
      i.duration,
      i.is_paid AS isPaid,
      i.min_stipend AS minStipend,
      i.max_stipend AS maxStipend,
      i.mode,
      i.location,
      i.internship_type AS internshipType,
      i.number_of_openings AS numberOfOpenings,
      i.is_immediate AS isImmediate,
      i.join_from AS joinFrom,
      i.join_to AS joinTo,
      i.application_deadline AS applicationDeadline,
      i.posted_by AS postedBy,
      i.is_active AS isActive,
      i.created_at AS createdAt,
      i.updated_at AS updatedAt,
      i.delete_at AS deleteAt,          
      e.company_name AS companyName,        
      e.logo_url AS logo,             
      false AS isBookmark,
      false AS isApplied
    FROM public.internship_post i
    JOIN employer e ON e.id = i.employer_id
    WHERE (:companyName IS NULL 
           OR LOWER(e.company_name) LIKE CONCAT('%', LOWER(CAST(:companyName AS text)), '%'))
      AND (:location    IS NULL 
           OR LOWER(i.location)    LIKE CONCAT('%', LOWER(CAST(:location AS text)), '%'))
      AND (:title       IS NULL 
           OR LOWER(i.title)       LIKE CONCAT('%', LOWER(CAST(:title AS text)), '%'))
      AND (:date        IS NULL 
           OR DATE(i.created_at) = DATE(CAST(:date AS timestamp)))
      AND i.delete_at IS NULL
""", nativeQuery = true)
    List<InternshipCardResponse> findAllInternships(
            @Param("title") String title,
            @Param("location") String location,
            @Param("companyName") String companyName,
            @Param("date") String date,
            Pageable pageable);

       //totalCount For Internship
            @Query(value = """
            	    SELECT COUNT(*)
            	    FROM public.internship_post i
            	    JOIN employer e ON e.id = i.employer_id
            	    WHERE (:companyName IS NULL 
            	           OR LOWER(e.company_name) LIKE CONCAT('%', LOWER(CAST(:companyName AS text)), '%'))
            	      AND (:location    IS NULL 
            	           OR LOWER(i.location)    LIKE CONCAT('%', LOWER(CAST(:location AS text)), '%'))
            	      AND (:title       IS NULL 
            	           OR LOWER(i.title)       LIKE CONCAT('%', LOWER(CAST(:title AS text)), '%'))
            	      AND (:date        IS NULL 
            	           OR DATE(i.created_at) = DATE(CAST(:date AS timestamp)))
            	      AND i.delete_at IS NULL
            	""", nativeQuery = true)
            	Long countFilteredInternships(
            	        @Param("title") String title,
            	        @Param("location") String location,
            	        @Param("companyName") String companyName,
            	        @Param("date") String date);


            

    @Modifying
    @Transactional
    @Query("UPDATE InternshipPost i SET i.isActive = :active WHERE i.id = :id")
    void activeAndInactive(@Param("id") String id, @Param("active") boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE InternshipPost i SET i.deleteAt = CURRENT_TIMESTAMP WHERE i.id = :id")
    void softDeleteInternshipPostById(@Param("id") String id);

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
            i.employer.companyName AS companyName,
            i.employer.logoUrl AS logo,
            false AS isBookmark,
            false AS isApplied
        FROM InternshipPost i
        WHERE i.deleteAt IS NULL AND i.isActive = true
        ORDER BY i.createdAt DESC
        """)
List<InternshipCardResponse> findTop5RecentInternships(Pageable pageable);


}
