package com.main.jobilitybackend.services.impl;

import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
import com.main.jobilitybackend.dto.requestDTO.rcnv.ProjectRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.ProjectResponse;
import com.main.jobilitybackend.dto.responseDTO.PublicProjectResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Category;
import com.main.jobilitybackend.entities.ClubProject;
import com.main.jobilitybackend.entities.Project;
import com.main.jobilitybackend.entities.SubCategory;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.repositories.CategoryRepo;
import com.main.jobilitybackend.repositories.ClubProjectRepo;
import com.main.jobilitybackend.repositories.ProjectRepo;
import com.main.jobilitybackend.repositories.SubCategoryRepo;
import com.main.jobilitybackend.services.serviceInterface.ProjectService;

import io.jsonwebtoken.io.IOException;

@Service
public class ProjectServiceImpl implements ProjectService {

        @Autowired
        private ProjectRepo projectRepo;

        @Autowired
        private ClubProjectRepo clubProjectRepo;
        @Autowired
        private CategoryRepo categoryRepo;
        @Autowired
        private SubCategoryRepo subCategoryRepo;

        @Autowired
        private ImageUploader imageUploader;
        
        @Value("${file.upload-dir}")
        private String uploadImagePath;

        @Override
        public DataResponse createProject(ProjectRequest projectRequest, List<MultipartFile> images) {
                ClubProject clubProject = clubProjectRepo.findById(projectRequest.getClubProjectId())
                                .filter(cp -> cp.getDeleteAt() == null)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "ClubProject not found with id: " + projectRequest.getClubProjectId()));
                Category category = categoryRepo.findById(projectRequest.getCategoryId())
                                .filter(c -> c.getDeleteAt() == null)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Category not found with id: " + projectRequest.getCategoryId()));
                SubCategory subCategory = subCategoryRepo.findById(projectRequest.getSubCategoryId())
                                .filter(sc -> sc.getDeleteAt() == null)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "SubCategory not found with id: " + projectRequest.getSubCategoryId()));

                Project project = new Project();
                project.setTitle(projectRequest.getTitle());
                project.setDetail(projectRequest.getDetail());
                project.setDate(projectRequest.getDate());
                project.setClubId(projectRequest.getClubId());
                project.setYear(projectRequest.getYear());
                project.setDistrictName(projectRequest.getDistrictName());
                project.setDestrictNo(projectRequest.getDestrictNo());
                project.setCost(projectRequest.getCost());
                project.setBeneficiaries(projectRequest.getBeneficiaries());
                project.setManHours(projectRequest.getManHours());
                project.setPresidentName(projectRequest.getPresidentName());
                project.setPresidentContact(projectRequest.getPresidentContact());
                project.setRotarians(projectRequest.getRotarians());
                project.setRotaractors(projectRequest.getRotaractors());
                project.setFacebookLink(projectRequest.getFacebookLink());
                project.setEmail(projectRequest.getEmail());
                project.setInstaLink(projectRequest.getInstaLink());
                project.setClubProject(clubProject);
                project.setCategory(category);
                project.setSubCategory(subCategory);
                project.setCreatedAt(LocalDateTime.now());
                // Handle images (if any)
                // You may want to store image URLs in a separate table or as a JSON field in
                // Project
                // For now, just upload and ignore the URLs
                List<String> imageUrls = new ArrayList<>();
                if (images != null && !images.isEmpty()) {
                        for (MultipartFile image : images) {
                                imageUrls.add(imageUploader.imageUploader(image));
                        }
                }
                project.setImageUrls(imageUrls);
                projectRepo.save(project);
                return DataResponse.builder()
                                .success(true)
                                .status(org.springframework.http.HttpStatus.CREATED)
                                .statusCode(201)
                                .message("Project created successfully")
                                .timestamp(Instant.now())
                                .data(project)
                                .build();
        }

        @Override
        public DataResponse getAllProjects(Long clubProjectId, Long categoryId, Long subCategoryId, String city,
                        String year, int page) {
                Pageable pageable = PageRequest.of(page, 10);
                List<Project> projects = projectRepo.findAllActiveByFilters(clubProjectId, categoryId, subCategoryId,
                                city, year, pageable);
                
                long totalCount = projectRepo.countAllActiveByFilters(clubProjectId, categoryId, subCategoryId, city, year);
                
                List<PublicProjectResponse> response = projects.stream().map(p -> {
                    return new PublicProjectResponse(
                        p.getId(), p.getTitle(), p.getDetail(), p.getDate(), p.getClubId(), p.getYear(), p.getDistrictName(), p.getDestrictNo(),
                        p.getPresidentName(), p.getPresidentContact(), p.getCost(), p.getBeneficiaries(), p.getManHours(),
                        p.getRotarians(), p.getRotaractors(), p.getFacebookLink(), p.getEmail(), p.getInstaLink(),
                        p.getImageUrls(), p.getCreatedAt(), p.getUpdatedAt(),
                        p.getSubCategory() != null ? p.getSubCategory().getId() : null,
                        p.getCategory() != null ? p.getCategory().getId() : null,
                        p.getClubProject() != null ? p.getClubProject().getId() : null
                    );
                }).toList();
                
                return DataResponse.builder()
                                .success(true)
                                .status(org.springframework.http.HttpStatus.OK)
                                .statusCode(200)
                                .totalCount(totalCount)
                                .message("Projects fetched successfully")
                                .timestamp(Instant.now())
                                .data(response)
                                .build();
        }

        
        @Override
        public DataResponse getProjectById(Long id) {
            Project project = projectRepo.findById(id)
                .filter(p -> p.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

            ProjectResponse response = new ProjectResponse();
            BeanUtils.copyProperties(project, response);

            response.setSubCategoryId(project.getSubCategory() != null ? project.getSubCategory().getId() : null);
            response.setCategoryId(project.getCategory() != null ? project.getCategory().getId() : null);
            response.setClubProjectId(project.getClubProject() != null ? project.getClubProject().getId() : null);

            return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Project fetched successfully")
                .timestamp(Instant.now())
                .data(response)
                .build();
        }


        @Override
        public SuccessResponse updateProject(Long id, ProjectRequest project, List<MultipartFile> images) {
            Project existing = projectRepo.findById(id)
                .filter(p -> p.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

            // Set basic fields
            existing.setTitle(project.getTitle());
            existing.setDetail(project.getDetail());
            existing.setDate(project.getDate());
            existing.setClubId(project.getClubId());
            existing.setYear(project.getYear());
            existing.setDistrictName(project.getDistrictName());
            existing.setDestrictNo(project.getDestrictNo());
            existing.setCost(project.getCost());
            existing.setBeneficiaries(project.getBeneficiaries());
            existing.setManHours(project.getManHours());
            existing.setRotarians(project.getRotarians());
            existing.setRotaractors(project.getRotaractors());
            existing.setFacebookLink(project.getFacebookLink());
            existing.setEmail(project.getEmail());
            existing.setInstaLink(project.getInstaLink());
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setClubProject(clubProjectRepo.findById(project.getClubProjectId()).get());
            existing.setCategory(categoryRepo.findById(project.getCategoryId()).get());
            existing.setSubCategory(subCategoryRepo.findById(project.getSubCategoryId()).get());
       

//            // Handle image upload
//            if (images != null && !images.isEmpty()) {
//                List<String> imagePaths = new ArrayList<>();
//                for (MultipartFile file : images) {
//                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//                    Path uploadPath = Paths.get(uploadImagePath+"/" + fileName);
//                    try {
//                        try {
//							Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
//						} catch (java.io.IOException e) {
//							
//							e.printStackTrace();
//						}
//                        imagePaths.add(uploadImagePath + "/" + fileName); 
//                    } catch (IOException e) {
//                        throw new RuntimeException("Failed to save image: " + fileName, e);
//                    }
//                }
//                existing.setImageUrls(imagePaths); 
//            }
            
            
         // Handle image upload using ImageUploader
            if (images != null && !images.isEmpty()) {
                List<String> existingUrls = existing.getImageUrls() != null 
                    ? new ArrayList<>(existing.getImageUrls()) 
                    : new ArrayList<>();

                for (MultipartFile file : images) {
                    String imageUrl = imageUploader.imageUploader(file);
                    existingUrls.add(imageUrl);
                }

                existing.setImageUrls(existingUrls);
            }


            projectRepo.save(existing);

            return SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Project updated successfully")
                .timestamp(Instant.now())
                .build();
        }


        @Override
        public SuccessResponse deleteProject(Long id) {
                Project project = projectRepo.findById(id)
                                .filter(p -> p.getDeleteAt() == null)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
                projectRepo.delete(project);
                return SuccessResponse.builder()
                                .success(true)
                                .status(HttpStatus.OK)
                                .statusCode(200)
                                .message("Project deleted successfully")
                                .timestamp(Instant.now())
                                .build();
        }

        @Override
        public DataResponse addProjectImages(long id, List<MultipartFile> images) {
                if (images == null || images.isEmpty()) {
                        return DataResponse.builder()
                                        .success(false)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .statusCode(400)
                                        .message("No images provided")
                                        .timestamp(Instant.now())
                                        .data(null)
                                        .build();
                }
                Project project = projectRepo.findById(id)
                                .filter(p -> p.getDeleteAt() == null)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
                List<String> urls = new ArrayList<>();
                for (MultipartFile image : images) {
                        String url = imageUploader.imageUploader(image);
                        urls.add(url);
                }
                if (project.getImageUrls() == null) {
                        project.setImageUrls(new ArrayList<>());
                }
                project.getImageUrls().addAll(urls);
                projectRepo.save(project);
                return DataResponse.builder()
                                .success(true)
                                .status(HttpStatus.OK)
                                .statusCode(200)
                                .message("Images uploaded and added to project successfully")
                                .timestamp(Instant.now())
                                .data(project.getImageUrls())
                                .build();
        }

        @Override
        public SuccessResponse softDeleteProject(long id) {
                Project project = projectRepo.findById(id)
                                .filter(p -> p.getDeleteAt() == null)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
                project.setDeleteAt(LocalDateTime.now());
                projectRepo.save(project);
                return SuccessResponse.builder()
                                .success(true)
                                .status(org.springframework.http.HttpStatus.OK)
                                .statusCode(200)
                                .message("Project soft deleted successfully")
                                .timestamp(Instant.now())
                                .build();
        }

}
