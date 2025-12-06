package com.main.jobilitybackend.services.serviceInterface;

import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;


public interface HeroSectionService {
    DataResponse addHeroSection(MultipartFile image,String title);
    DataResponse getHeroSection();
    DataResponse getHeroSectionById(long id);
    SuccessResponse updateHeroSection(long id,MultipartFile image,String title);
    SuccessResponse softDeleteHeroSection(long id);
}
