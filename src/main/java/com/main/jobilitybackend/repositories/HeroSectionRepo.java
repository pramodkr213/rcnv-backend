package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.main.jobilitybackend.entities.HeroSection;

public interface HeroSectionRepo extends JpaRepository<HeroSection, Long> {
    
    //List<HeroSection> findByDeleteAtIsNull();
    
    List<HeroSection> findByDeleteAtIsNullOrderByCreatedAtDesc();
    //Query for count 
    @Query("SELECT COUNT(h) FROM HeroSection h WHERE h.deleteAt IS NULL")
    long countAllActiveHeroSections();

    @Query("SELECT h FROM HeroSection h WHERE h.id = :id AND h.deleteAt IS NULL")
    Optional<HeroSection> findByIdAndDeleteAtIsNull(@Param("id") Long id);

    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE HeroSection h SET h.deleteAt = CURRENT_TIMESTAMP WHERE h.id = :id")
    void softDeleteHeroSection(@Param("id") long id);
} 