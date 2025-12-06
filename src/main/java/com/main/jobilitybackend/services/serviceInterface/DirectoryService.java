package com.main.jobilitybackend.services.serviceInterface;

import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.rcnv.DirectorRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Media;


public interface DirectoryService {
    SuccessResponse addDirectory(DirectorRequest request,MultipartFile image);
    DataResponse getAllDirectories();
    DataResponse getDirectoryById(Long id);
    SuccessResponse updateDirectory(Long id, DirectorRequest request, MultipartFile image);
    SuccessResponse softDeleteDirectory(Long id);
    SuccessResponse deleteDirectory(Long id);
	Media saveMedia(MultipartFile image);
}
