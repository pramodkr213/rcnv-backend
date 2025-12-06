package com.main.jobilitybackend.services.serviceInterface;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;

public interface SubCategoryService {
    DataResponse createSubCategory(long categoryId,String title);
    DataResponse getAllSubCategoriesByCategory(long categoryId);
    DataResponse getAllSubCategories();
    DataResponse getSubCategoryById(Long id);
    SuccessResponse updateSubCategory(Long id, String title);
    SuccessResponse deleteSubCategory(Long id);
    SuccessResponse softDeleteCategory(long id);
} 