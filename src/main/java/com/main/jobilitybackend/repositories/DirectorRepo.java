package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Director;

public interface DirectorRepo extends JpaRepository<Director,Long>{
    
}
