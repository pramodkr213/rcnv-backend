package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface CategoryService {
    DataResponse createCategory(long clubId,String name);
    DataResponse getAllCategories();
    DataResponse getAllCategoriesByClub(long clubId);
    DataResponse getCategoryById(Long id);
    SuccessResponse updateCategory(Long id, String name);
    SuccessResponse deleteCategory(Long id);
    SuccessResponse softDeleteCategory(Long id);
} 