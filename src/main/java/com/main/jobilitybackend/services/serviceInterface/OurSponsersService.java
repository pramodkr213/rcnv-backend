package com.main.jobilitybackend.services.serviceInterface;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;


public interface OurSponsersService {
    
    // Create
    DataResponse createOurSponser(List<MultipartFile> images);

    DataResponse getAllOurSponsers();

    // Soft Delete
    SuccessResponse softDeleteOurSponser(Long id);

    // Hard Delete
    SuccessResponse deleteOurSponser(Long id);
}
