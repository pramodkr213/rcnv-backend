package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.main.jobilitybackend.entities.OurSponsers;

import org.springframework.data.jpa.repository.Modifying;

public interface OurSponsersRepo extends JpaRepository<OurSponsers, Long> {
    
    List<OurSponsers> findByDeleteAtIsNull();

    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE OurSponsers o SET o.deleteAt = CURRENT_TIMESTAMP WHERE o.id = :id")
    void softDeleteOurSponsers(@Param("id") long id);
} 