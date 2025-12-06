package com.main.jobilitybackend.services.impl;

import java.util.List;
import java.time.Instant;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.OurSponsers;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.services.serviceInterface.OurSponsersService;
import com.main.jobilitybackend.repositories.OurSponsersRepo;

@Service
public class OurSponsersServiceImpl implements OurSponsersService {

    @Autowired
    private OurSponsersRepo ourSponsersRepo;

    @Autowired
    private ImageUploader imageUploader;

    @Override
    public DataResponse createOurSponser(List<MultipartFile> images) {
        // Upload each image and create an OurSponsers entity for each
        ArrayList<OurSponsers> savedSponsers = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl = imageUploader.imageUploader(image);
            OurSponsers sponser = new OurSponsers();
            sponser.setImageUrl(imageUrl);
            savedSponsers.add(ourSponsersRepo.save(sponser));
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .data(savedSponsers)
                .message("Sponsor images added successfully")
                .build();
        return response;
    }

    @Override
    public DataResponse getAllOurSponsers() {
        List<OurSponsers> sponsers = this.ourSponsersRepo.findByDeleteAtIsNull();
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .data(sponsers)
                .message("Fetched all sponsors successfully")
                .build();
        return response;
    }

    @Override
    public SuccessResponse softDeleteOurSponser(Long id) {
        this.ourSponsersRepo.softDeleteOurSponsers(id);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .message("Sponsor soft deleted successfully")
                .build();
        return response;
    }

    @Override
    public SuccessResponse deleteOurSponser(Long id) {
        this.ourSponsersRepo.deleteById(id);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .message("Sponsor hard deleted successfully")
                .build();
        return response;
    }

}
