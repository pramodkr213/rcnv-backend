package com.main.jobilitybackend.services.impl;

import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.ClubProject;
import com.main.jobilitybackend.services.serviceInterface.ClubProjectService;
import com.main.jobilitybackend.repositories.ClubProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClubProjectServiceImpl implements ClubProjectService{

    @Autowired
    private ClubProjectRepo clubProjectRepo;

    @Override
    public DataResponse createClubProject(String name) {
        // Check for duplicate name (active only)
        List<ClubProject> existing = clubProjectRepo.findAll();
        if (existing.stream().anyMatch(cp -> cp.getName().equalsIgnoreCase(name) && cp.getDeleteAt() == null)) {
            throw new DuplicateEntityException("ClubProject with this name already exists");
        }
        ClubProject clubProject = new ClubProject();
        clubProject.setName(name);
        clubProject.setCreatedAt(LocalDateTime.now());
        clubProjectRepo.save(clubProject);
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.CREATED)
                .statusCode(201)
                .message("ClubProject created successfully")
                .timestamp(Instant.now())
                .data(clubProject)
                .build();
    }

    @Override
    public DataResponse getAllClubProjects() {
        List<ClubProject> clubProjects = clubProjectRepo.findAll().stream()
                .filter(cp -> cp.getDeleteAt() == null)
                .toList();
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("All ClubProjects fetched successfully")
                .timestamp(Instant.now())
                .data(clubProjects)
                .build();
    }

    @Override
    public DataResponse getClubProjectById(Long id) {
        ClubProject clubProject = clubProjectRepo.findById(id)
                .filter(cp -> cp.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("ClubProject not found with id: " + id));
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("ClubProject fetched successfully")
                .timestamp(Instant.now())
                .data(clubProject)
                .build();
    }

    @Override
    public SuccessResponse updateClubProject(Long id, ClubProject clubProject) {
        ClubProject existing = clubProjectRepo.findById(id)
                .filter(cp -> cp.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("ClubProject not found with id: " + id));
        existing.setName(clubProject.getName());
        existing.setUpdatedAt(LocalDateTime.now());
        clubProjectRepo.save(existing);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("ClubProject updated successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse deleteClubProject(Long id) {
        ClubProject clubProject = clubProjectRepo.findById(id)
                .filter(cp -> cp.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("ClubProject not found with id: " + id));
        clubProject.setDeleteAt(LocalDateTime.now());
        clubProjectRepo.save(clubProject);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("ClubProject deleted successfully")
                .timestamp(Instant.now())
                .build();
    }
    
}
