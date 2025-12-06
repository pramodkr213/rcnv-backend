package com.main.jobilitybackend.services.serviceInterface;


import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.rcnv.GalleryRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Gallery;
import com.main.jobilitybackend.entities.Imagecategory;
import com.main.jobilitybackend.entities.Media;

public interface GalleryService {
    // Create
    DataResponse createGallery(Long catid, List<MultipartFile> images);

    DataResponse getAllGallery(Integer page, Integer size);
    DataResponse getGalleryById(Long id);

    // Update
    SuccessResponse updateGallery(Long id, MultipartFile image, GalleryRequest request);

    // Soft Delete
    SuccessResponse softDeleteGallery(Long id);

    // Hard Delete
    SuccessResponse deleteGallery(Long id);

	DataResponse createMedia(List<MultipartFile> images);

	DataResponse deleteMedia(Long id);


	Map<String, Object> getAllMedia(int page, int size);

	ResponseEntity<?> findByCatid(Long catid, int page, int size);

	ResponseEntity<?> getAllByCatIdwithoupagination(Long catid);

	ResponseEntity<?> findByCidWithoutpagination(Long cid);

	DataResponse updateMedia(List<MultipartFile> images, Long id);
}
