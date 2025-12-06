package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
   
	@Query("SELECT c, p.name FROM Category c JOIN c.clubProject p WHERE c.deleteAt IS NULL")
	List<Object[]> findAllActive();

    @Query("SELECT c FROM Category c WHERE c.clubProject.id = :clubId AND c.deleteAt IS NULL")
    List<Category> findAllActiveByClubProjectId(@Param("clubId") long clubId);
} 