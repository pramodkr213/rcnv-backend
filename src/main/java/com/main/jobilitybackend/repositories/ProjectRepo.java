package com.main.jobilitybackend.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.entities.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    
    @Query("SELECT p FROM Project p WHERE p.deleteAt IS NULL")
    List<Project> findAllActive();

//    @Query("""
//        SELECT p FROM Project p 
//        WHERE (:clubProjectId IS NULL OR p.clubProject.id = :clubProjectId) 
//        AND (:categoryId IS NULL OR p.category.id = :categoryId) 
//        AND (:subCategoryId IS NULL OR p.subCategory.id = :subCategoryId) 
//        AND (:city IS NULL OR p.districtName = :city)
//        AND (:year IS NULL OR p.year = :year)
//        AND p.deleteAt IS NULL
//    """)
//    List<Project> findAllActiveByFilters(@Param("clubProjectId") Long clubProjectId, 
//                                        @Param("categoryId") Long categoryId, 
//                                        @Param("subCategoryId") Long subCategoryId,
//                                        @Param("city") String city,
//                                        @Param("year") String year,
//                                        Pageable pageable);
    
    @Query("""
    	    SELECT p FROM Project p 
    	    WHERE (:clubProjectId IS NULL OR p.clubProject.id = :clubProjectId) 
    	    AND (:categoryId IS NULL OR p.category.id = :categoryId) 
    	    AND (:subCategoryId IS NULL OR p.subCategory.id = :subCategoryId) 
    	    AND (:city IS NULL OR p.districtName = :city)
    	    AND (:year IS NULL OR p.year = :year)
    	    AND p.deleteAt IS NULL
    	""")
    	List<Project> findAllActiveByFilters(@Param("clubProjectId") Long clubProjectId, 
              @Param("categoryId") Long categoryId, 
              @Param("subCategoryId") Long subCategoryId,
              @Param("city") String city,
              @Param("year") String year,
              Pageable pageable);

    
    // total count for all projects
    @Query("""
    	    SELECT COUNT(p) FROM Project p 
    	    WHERE (:clubProjectId IS NULL OR p.clubProject.id = :clubProjectId) 
    	    AND (:categoryId IS NULL OR p.category.id = :categoryId) 
    	    AND (:subCategoryId IS NULL OR p.subCategory.id = :subCategoryId) 
    	    AND (:city IS NULL OR p.districtName = :city)
    	    AND (:year IS NULL OR p.year = :year)
    	    AND p.deleteAt IS NULL
    	""")
    	long countAllActiveByFilters(@Param("clubProjectId") Long clubProjectId, 
    	                             @Param("categoryId") Long categoryId, 
    	                             @Param("subCategoryId") Long subCategoryId,
    	                             @Param("city") String city,
    	                             @Param("year") String year);

	List<Project> findBySubCategoryId(Long id);

} 
