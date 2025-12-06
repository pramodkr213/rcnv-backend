package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.entities.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    
    List<SubCategory> findByCategoryId(long categoryId);
    
//    @Query("""
//    	    SELECT 
//    	        s.name AS name,
//    	        c.name AS category_name,
//    	        cp.name AS club_name,
//    	        s.id AS id
//    	    FROM 
//    	        SubCategory s
//    	    LEFT JOIN Category c ON s.id = c.id
//    	    LEFT JOIN ClubProject cp ON c.clubProject.id = cp.id
//    	    WHERE s.deleteAt IS NULL
//    	""")
//    	List<Map<String, String>> findAllActive();
    
    @Query("""
    	    SELECT 
    	        s.name AS name,
    	        c.name AS category_name,
    	        cp.name AS club_name,
    	        s.id AS id
    	    FROM 
    	        SubCategory s
    	    LEFT JOIN s.category c
    	    LEFT JOIN c.clubProject cp
    	    WHERE s.deleteAt IS NULL
    	""")
    	List<Map<String, Object>> findAllActive();


    
    @Query("SELECT s FROM SubCategory s WHERE s.category.id = :categoryId AND s.deleteAt IS NULL")
    List<SubCategory> findAllActiveByCategoryId(@Param("categoryId") long categoryId);
} 