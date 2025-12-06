package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.ClubProject;

public interface ClubProjectRepo extends JpaRepository<ClubProject, Long> {

    boolean existsByDeleteAtIsNull();
} 