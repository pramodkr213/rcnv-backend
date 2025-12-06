package com.main.jobilitybackend.services.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.HeroSection;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.repositories.HeroSectionRepo;
import com.main.jobilitybackend.services.serviceInterface.HeroSectionService;

@Service
public class HeroSectionServiceImpl implements HeroSectionService {

    @Autowired
    private HeroSectionRepo heroSectionRepo;

    @Autowired
    private ImageUploader imageUploader;

    @Override
    public DataResponse addHeroSection(MultipartFile image, String title) {
        HeroSection hero = new HeroSection();
        hero.setTitle(title);
        String imageUrl = imageUploader.imageUploader(image);
        hero.setImageUrl(imageUrl);
        hero = this.heroSectionRepo.save(hero);
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .data(hero)
                .message("Hero Section add Successfully ")
                .build();
        return response;
    }

//    @Override
//    public DataResponse getHeroSection() {
//        var heroSections = this.heroSectionRepo.findByDeleteAtIsNull();
//        DataResponse response = DataResponse.builder()
//                .success(true)
//                .status(HttpStatus.OK)
//                .statusCode(200)
//                .timestamp(Instant.now())
//                .data(heroSections)
//                .message("Fetched all hero sections successfully")
//                .build();
//        return response;
//    }
    
    @Override
    public DataResponse getHeroSection() {
        List<HeroSection> heroSections = this.heroSectionRepo.findByDeleteAtIsNullOrderByCreatedAtDesc();
        long totalCount = this.heroSectionRepo.countAllActiveHeroSections();
        DataResponse response = DataResponse.builder().success(true).status(HttpStatus.OK).statusCode(200).timestamp(Instant.now()).totalCount(totalCount).data(heroSections).message("Fetched all hero sections successfully").build();
        return response;
      }

    @Override
    public DataResponse getHeroSectionById(long id) {
        var heroSectionOpt = this.heroSectionRepo.findByIdAndDeleteAtIsNull(id);
        if (heroSectionOpt.isPresent()) {
            DataResponse response = DataResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .timestamp(Instant.now())
                    .data(heroSectionOpt.get())
                    .message("Fetched hero section successfully")
                    .build();
            return response;
        } else {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .timestamp(Instant.now())
                    .data(null)
                    .message("Hero section not found")
                    .build();
            return response;
        }
    }

    @Override
    public SuccessResponse updateHeroSection(long id, MultipartFile image, String title) {
        var heroSectionOpt = this.heroSectionRepo.findByIdAndDeleteAtIsNull(id);
        if (heroSectionOpt.isPresent()) {
            HeroSection hero = heroSectionOpt.get();
            hero.setTitle(title);
            if (image != null && !image.isEmpty()) {
                String imageUrl = imageUploader.imageUploader(image);
                hero.setImageUrl(imageUrl);
            }
            this.heroSectionRepo.save(hero);
            SuccessResponse response = SuccessResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .timestamp(Instant.now())
                    .message("Hero section updated successfully")
                    .build();
            return response;
        } else {
            SuccessResponse response = SuccessResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .timestamp(Instant.now())
                    .message("Hero section not found")
                    .build();
            return response;
        }
    }

    @Override
    public SuccessResponse softDeleteHeroSection(long id) {
        this.heroSectionRepo.softDeleteHeroSection(id);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .message("Hero section soft deleted successfully")
                .build();
        return response;
    }

}
