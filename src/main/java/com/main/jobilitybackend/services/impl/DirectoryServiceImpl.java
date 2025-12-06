package com.main.jobilitybackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.services.serviceInterface.DirectoryService;
import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
import com.main.jobilitybackend.dto.requestDTO.rcnv.DirectorRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Directory;
import com.main.jobilitybackend.entities.Media;
import com.main.jobilitybackend.repositories.DirectoryRepo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectoryServiceImpl implements DirectoryService{

    @Autowired
    private DirectoryRepo directoryRepo;

    @Override
    public SuccessResponse addDirectory(DirectorRequest request, MultipartFile image) {
        // Check for duplicate (active only)
        List<Directory> existing = directoryRepo.findAll();
        boolean duplicate = existing.stream()
            .anyMatch(d -> d.getName().equalsIgnoreCase(request.getName()) && d.getDeleteAt() == null);
        if (duplicate) {
            throw new DuplicateEntityException("Directory with this name already exists");
        }
        Directory directory = new Directory();
        directory.setName(request.getName());
        directory.setEmail(request.getEmail());
        directory.setDesignation(request.getDesignation());
        directoryRepo.save(directory);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.CREATED)
                .statusCode(201)
                .message("Directory created successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public DataResponse getAllDirectories() {
        List<Directory> directories = directoryRepo.findAll().stream()
                .filter(d -> d.getDeleteAt() == null)
                .collect(Collectors.toList());
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("All directories fetched successfully")
                .timestamp(Instant.now())
                .data(directories)
                .build();
    }

    @Override
    public DataResponse getDirectoryById(Long id) {
        Directory directory = directoryRepo.findById(id)
                .filter(d -> d.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Directory not found with id: " + id));
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Directory fetched successfully")
                .timestamp(Instant.now())
                .data(directory)
                .build();
    }

    @Override
    public SuccessResponse updateDirectory(Long id, DirectorRequest request, MultipartFile image) {
        Directory directory = directoryRepo.findById(id)
                .filter(d -> d.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Directory not found with id: " + id));
        directory.setName(request.getName());
        directory.setEmail(request.getEmail());
        directory.setDesignation(request.getDesignation());
        directory.setUpdatedAt(LocalDateTime.now());
        directoryRepo.save(directory);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Directory updated successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse softDeleteDirectory(Long id) {
        Directory directory = directoryRepo.findById(id)
                .filter(d -> d.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Directory not found with id: " + id));
        directory.setDeleteAt(LocalDateTime.now());
        directoryRepo.save(directory);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Directory soft deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse deleteDirectory(Long id) {
        Directory directory = directoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Directory not found with id: " + id));
        directoryRepo.delete(directory);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Directory deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

	@Override
	public Media saveMedia(MultipartFile image) {
		// TODO Auto-generated method stub
		return null;
	}


    
}
