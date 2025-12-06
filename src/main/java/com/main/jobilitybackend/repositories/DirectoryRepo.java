package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Directory;

public interface DirectoryRepo extends JpaRepository<Directory,Long>{
    
}