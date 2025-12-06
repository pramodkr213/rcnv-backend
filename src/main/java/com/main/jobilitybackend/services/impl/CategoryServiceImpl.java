package com.main.jobilitybackend.services.impl;

import com.main.jobilitybackend.Exceptions.DuplicateEntityException;
import com.main.jobilitybackend.Exceptions.EntityNotFoundException;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Category;
import com.main.jobilitybackend.entities.ClubProject;
import com.main.jobilitybackend.services.serviceInterface.CategoryService;
import com.main.jobilitybackend.repositories.CategoryRepo;
import com.main.jobilitybackend.repositories.ClubProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepo categoryRepo;
    
    @Autowired
    private ClubProjectRepo clubProjectRepo;

    @Override
    public DataResponse createCategory(long clubprojectId, String name) {
        // Validate club project exists
        ClubProject clubProject = clubProjectRepo.findById(clubprojectId)
                .filter(cp -> cp.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Club project not found with id: " + clubprojectId));
        
        // Check if category with same name already exists for this club
        List<Category> existingCategories = categoryRepo.findAllActiveByClubProjectId(clubprojectId);
        boolean categoryExists = existingCategories.stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(name));
        
        if (categoryExists) {
            throw new DuplicateEntityException("Category with name '" + name + "' already exists for this club");
        }
        
        // Create new category
        Category category = new Category();
        category.setName(name);
        category.setClubProject(clubProject);
        
        Category savedCategory = categoryRepo.save(category);
        
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.CREATED)
                .statusCode(201)
                .message("Category created successfully")
                .timestamp(Instant.now())
                .data(savedCategory)
                .build();
    }

    @Override
    public DataResponse getAllCategories() {

        List<Object[]> result = categoryRepo.findAllActive();

        List<Map<String, Object>> categoryData = result.stream()
            .map(row -> {
                Category category = (Category) row[0];
                String clubName = (String) row[1];

                Map<String, Object> map = new HashMap<>();
                map.put("id", category.getId());
                map.put("name", category.getName());
                map.put("clubName", clubName);
                return map;
            })
            .toList();


        return DataResponse.builder()
            .success(true)
            .status(HttpStatus.OK)
            .statusCode(200)
            .message("All Categories fetched successfully")
            .timestamp(Instant.now())
            .data(categoryData)
            .build();
    }


    @Override
    public DataResponse getAllCategoriesByClub(long clubId) {
        List<Category> categories = categoryRepo.findAllActiveByClubProjectId(clubId);
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Categories for club fetched successfully")
                .timestamp(Instant.now())
                .data(categories)
                .build();
    }

    @Override
    public DataResponse getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .filter(c -> c.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return DataResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Category fetched successfully")
                .timestamp(Instant.now())
                .data(category)
                .build();
    }

    @Override
    public SuccessResponse updateCategory(Long id, String name) {
        // Find the category to update
        Category category = categoryRepo.findById(id)
                .filter(c -> c.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        // Check if another category with the same name already exists in the same club
        List<Category> existingCategories = categoryRepo.findAllActiveByClubProjectId(category.getClubProject().getId());
        boolean duplicateName = existingCategories.stream()
                .anyMatch(cat -> cat.getId() != id && cat.getName().equalsIgnoreCase(name));
        
        if (duplicateName) {
            throw new DuplicateEntityException("Category with name '" + name + "' already exists for this club");
        }
        
        // Update the category name
        category.setName(name);
        categoryRepo.save(category);
        
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Category updated successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .filter(c -> c.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        categoryRepo.delete(category);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Category deleted successfully")
                .timestamp(Instant.now())
                .build();
    }

    @Override
    public SuccessResponse softDeleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .filter(c -> c.getDeleteAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        category.setDeleteAt(LocalDateTime.now());
        categoryRepo.save(category);
        return SuccessResponse.builder()
                .success(true)
                .status(org.springframework.http.HttpStatus.OK)
                .statusCode(200)
                .message("Category soft deleted successfully")
                .timestamp(Instant.now())
                .build();
    }
    
}
