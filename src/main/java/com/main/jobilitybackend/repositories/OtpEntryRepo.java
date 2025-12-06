package com.main.jobilitybackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.OtpEntry;

import jakarta.transaction.Transactional;

public interface OtpEntryRepo extends JpaRepository<OtpEntry,Long>{
    
    Optional<OtpEntry> findByEmail(String email);
    
    @Transactional
    void deleteByEmail(String email);

}
