package com.main.jobilitybackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
import com.main.jobilitybackend.dto.requestDTO.rcnv.DirectorRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Director;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.repositories.DirectorRepo;
import com.main.jobilitybackend.services.serviceInterface.DirectorService;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private DirectorRepo directorRepo;

    @Autowired
    private ImageUploader imageUploader;

    @Override
    public SuccessResponse addDirector(DirectorRequest request, MultipartFile image) {
        // Check for duplicate (active only)
        List<Director> existing = directorRepo.findAll();
        boolean duplicate = existing.stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(request.getName()) && d.getDeleteAt() == null);
        if (duplicate) {
            throw new DuplicateEntityException("Director with this name already exists");
        }
        Director director = new Director();
        director.setName(request.getName());
        director.setEmail(request.getEmail());
        director.setDesignation(request.getDesignation());
        director.setStartDate(request.getStartDate());

        if (image != null && !image.isEmpty()) {
            director.setImageUrl(imageUploader.imageUploader(image));
        }
        directorRepo.save(director);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.CREATED)
                .statusCode(201)
                .message("Director created successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public DataResponse getAllDirectors() {
        List<Director> directors = directorRepo.findAll().stream()
                .filter(d -> d.getDeleteAt() == null)
                .collect(Collectors.toList());
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("All directors fetched successfully")
                .timestamp(Instant.now())
                .data(directors)
                .build();
    }

    @Override
    public DataResponse getDirectorById(Long id) {
        Director director = directorRepo.findById(id)
                .filter(d -> d.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Director not found with id: " + id));
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Director fetched successfully")
                .timestamp(Instant.now())
                .data(director)
                .build();
    }

    @Override
    public SuccessResponse updateDirector(Long id, DirectorRequest request, MultipartFile image) {
        Director director = directorRepo.findById(id)
                .filter(d -> d.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Director not found with id: " + id));
        director.setName(request.getName());
        director.setEmail(request.getEmail());
        director.setDesignation(request.getDesignation());
        director.setStartDate(request.getStartDate());
        if(request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
            director.setEndDate(request.getEndDate()); 
        }
        if (image != null && !image.isEmpty()) {
            director.setImageUrl(imageUploader.imageUploader(image));
        }
        director.setUpdatedAt(LocalDateTime.now());
        directorRepo.save(director);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Director updated successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse softDeleteDirector(Long id) {
        Director director = directorRepo.findById(id)
                .filter(d -> d.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Director not found with id: " + id));
        director.setDeleteAt(LocalDateTime.now());
        directorRepo.save(director);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Director soft deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse deleteDirector(Long id) {
        Director director = directorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Director not found with id: " + id));
        directorRepo.delete(director);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Director deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

}
