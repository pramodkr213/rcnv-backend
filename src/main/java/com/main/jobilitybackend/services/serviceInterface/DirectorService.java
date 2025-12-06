package com.main.jobilitybackend.services.serviceInterface;

import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.rcnv.DirectorRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;


public interface DirectorService {
    SuccessResponse addDirector(DirectorRequest request,MultipartFile image);
    DataResponse getAllDirectors();
    DataResponse getDirectorById(Long id);
    SuccessResponse updateDirector(Long id, DirectorRequest request, MultipartFile image);
    SuccessResponse softDeleteDirector(Long id);
    SuccessResponse deleteDirector(Long id);
}
