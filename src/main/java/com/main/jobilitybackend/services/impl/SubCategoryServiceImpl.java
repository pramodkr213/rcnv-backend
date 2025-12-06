package com.main.jobilitybackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.repositories.SubCategoryRepo;
import com.main.jobilitybackend.services.serviceInterface.SubCategoryService;

import jakarta.transaction.Transactional;

import com.main.jobilitybackend.entities.SubCategory;
import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Category;
import com.main.jobilitybackend.entities.Project;
import com.main.jobilitybackend.repositories.CategoryRepo;
import com.main.jobilitybackend.repositories.ProjectRepo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{

    @Autowired
    private SubCategoryRepo subCategoryRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    
    @Autowired
    private ProjectRepo projectRepo;

    @Override
    public DataResponse createSubCategory(long categoryId, String title) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
        // Check for duplicate name in the same category
        List<SubCategory> existing = subCategoryRepo.findByCategoryId(categoryId);
        if (existing.stream().anyMatch(s -> s.getName().equalsIgnoreCase(title) && s.getDeleteAt() == null)) {
            throw new DuplicateEntityException("SubCategory with this name already exists in the category");
        }
        SubCategory subCategory = new SubCategory();
        subCategory.setName(title);
        subCategory.setCategory(category);
        subCategory.setCreatedAt(LocalDateTime.now());
        subCategoryRepo.save(subCategory);
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.CREATED)
                .statusCode(201)
                .message("SubCategory created successfully")
                .timestamp(Instant.now())
                .data(subCategory)
                .build();
    }

    @Override
    public DataResponse getAllSubCategoriesByCategory(long categoryId) {
        categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
        List<SubCategory> subCategories = subCategoryRepo.findAllActiveByCategoryId(categoryId);
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("SubCategories fetched successfully")
                .timestamp(Instant.now())
                .data(subCategories)
                .build();
    }

    @Override
    public DataResponse getAllSubCategories() {
        //List<SubCategory> subCategories = subCategoryRepo.findAllActive();
        List<Map<String, Object>> subCategories = subCategoryRepo.findAllActive();

        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("All SubCategories fetched successfully")
                .timestamp(Instant.now())
                .data(subCategories)
                .build();
    }

    @Override
    public DataResponse getSubCategoryById(Long id) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .filter(s -> s.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("SubCategory not found with id: " + id));
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("SubCategory fetched successfully")
                .timestamp(Instant.now())
                .data(subCategory)
                .build();
    }

    @Override
    public SuccessResponse updateSubCategory(Long id, String title) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .filter(s -> s.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("SubCategory not found with id: " + id));
        subCategory.setName(title);
        subCategory.setUpdatedAt(LocalDateTime.now());
        subCategoryRepo.save(subCategory);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("SubCategory updated successfully")
                .timestamp(Instant.now())
                .build();
    }

//    @Override
//    public SuccessResponse deleteSubCategory(Long id) {
//        SubCategory subCategory = subCategoryRepo.findById(id)
//                .filter(s -> s.getDeleteAt() == null)
//                .orElseThrow(() -> new EntityNotFoundException("SubCategory not found with id: " + id));
//        subCategoryRepo.delete(subCategory);
//        return SuccessResponse.builder()
//                .success(true)
//                .status(org.springframework.http.HttpStatus.OK)
//                .statusCode(200)
//                .message("SubCategory deleted successfully")
//                .timestamp(Instant.now())
//                .build();
//    }
    
    @Override
    @Transactional
    public SuccessResponse deleteSubCategory(Long id) {
      
        SubCategory subCategory = subCategoryRepo.findById(id)
            .filter(s -> s.getDeleteAt() == null)
            .orElseThrow(() -> new EntityNotFoundException("SubCategory not found with id: " + id));

        List<Project> projects = projectRepo.findBySubCategoryId(id);

        for (Project project : projects) {
            project.setSubCategory(null);
        }
        projectRepo.saveAll(projects);
        subCategoryRepo.delete(subCategory);
        
        return SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("SubCategory deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse softDeleteCategory(long id) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .filter(s -> s.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("SubCategory not found with id: " + id));
        subCategory.setDeleteAt(LocalDateTime.now());
        subCategoryRepo.save(subCategory);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("SubCategory soft deleted successfully")
                .timestamp(Instant.now())
                .build();
    }
    
}
