package com.main.jobilitybackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.jobilitybackend.entities.Education;

import jakarta.transaction.Transactional;

public interface EducationRepo extends JpaRepository<Education,Long>{
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Education e WHERE e.id = :id AND e.student.email = :userId")
    void deleteByIdAndStudentEmail(@Param("id")Long id,@Param("userId") String userId);

    List<Education> findByStudentId(String id);

    Optional<Education> findByIdAndStudentId(Long id, String studentId);
    
    Long countByStudentIdAndDeleteAtIsNull(String studentId);
}
