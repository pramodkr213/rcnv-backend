package com.main.jobilitybackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.main.jobilitybackend.entities.Gallery;

public interface GalleryRepo extends JpaRepository<Gallery, Long> {
    
    Page<Gallery> findByDeleteAtIsNull(Pageable pageable);
    
    @Query("SELECT COUNT(g) FROM Gallery g WHERE g.deleteAt IS NULL")
    long countAllActiveGalleries();

    @Query("SELECT g FROM Gallery g WHERE g.id = :id AND g.deleteAt IS NULL")
    Optional<Gallery> findByIdAndDeleteAtIsNull(@Param("id") Long id);
    List<Gallery> findByDeleteAtIsNull();
    void deleteById(Long id);



    Page<Gallery> findByCatidAndDeleteAtIsNull(Long catid, Pageable pageable);

	ResponseEntity<?> findByCatid(Long catid);

	   List<Gallery> findByCatidAndDeleteAtIsNull(Long catid);


	
}