package com.main.jobilitybackend.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    boolean existsByDeleteAtIsNull();
    boolean existsByEmailAndDeleteAtIsNull(String email);
}
