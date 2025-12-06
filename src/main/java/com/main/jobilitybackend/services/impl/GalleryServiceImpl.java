package com.main.jobilitybackend.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Optional;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.jobilitybackend.dto.requestDTO.rcnv.GalleryRequest;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.dto.responseDTO.SuccessResponse;
import com.main.jobilitybackend.entities.Gallery;
import com.main.jobilitybackend.entities.Imagecategory;
import com.main.jobilitybackend.entities.Media;
import com.main.jobilitybackend.helper.ImageUploader;
import com.main.jobilitybackend.services.serviceInterface.GalleryService;
import com.main.jobilitybackend.repositories.GalleryRepo;
import com.main.jobilitybackend.repositories.MediaRepository;

@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryRepo galleryRepo;
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private ImageUploader imageUploader;
    @Override
    public DataResponse getAllGallery(Integer page, Integer size) {
        List<Gallery> galleries;
        long totalCount;
        Map<String, Object> meta = null;

        if (page != null && size != null) {
            // Paginated fetch
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Gallery> galleryPage = this.galleryRepo.findByDeleteAtIsNull(pageRequest);
            galleries = galleryPage.getContent();
            totalCount = this.galleryRepo.countAllActiveGalleries();

            // Populate pagination metadata
            meta = new HashMap<>();
            meta.put("totalPages", galleryPage.getTotalPages());
            meta.put("currentPage", galleryPage.getNumber());
            meta.put("pageSize", galleryPage.getSize());
            meta.put("isLast", galleryPage.isLast());
            meta.put("isFirst", galleryPage.isFirst());
        } else {
            // Fetch all if no pagination params
            galleries = this.galleryRepo.findByDeleteAtIsNull();
            totalCount = galleries.size();
        }

        return DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .totalCount(totalCount)
                .data(galleries)
                .meta(meta) // ← ✅ This was missing in your previous code
                .message("Fetched gallery data successfully")
                .build();
    }

    @Override
    public DataResponse getGalleryById(Long id) {
        Optional<Gallery> galleryOpt = this.galleryRepo.findByIdAndDeleteAtIsNull(id);
        if (galleryOpt.isPresent()) {
            DataResponse response = DataResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .timestamp(Instant.now())
                    .data(galleryOpt.get())
                    .message("Fetched gallery successfully")
                    .build();
            return response;
        } else {
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .timestamp(Instant.now())
                    .data(null)
                    .message("Gallery not found")
                    .build();
            return response;
        }
    }

    @Override
    public SuccessResponse updateGallery(Long id, MultipartFile image, GalleryRequest request) {
        Optional<Gallery> galleryOpt = this.galleryRepo.findByIdAndDeleteAtIsNull(id);
        if (galleryOpt.isPresent()) {
            Gallery gallery = galleryOpt.get();
            if (request != null) {
                gallery.setTitle(request.getTitle());
                gallery.setDetail(request.getDetail());
                gallery.setCatid(request.getCatid() != null?request.getCatid():gallery.getCatid());
            }
            if (image != null && !image.isEmpty()) {
                String imageUrl = imageUploader.imageUploader(image);
                gallery.setImageUrl(imageUrl);
            }
            this.galleryRepo.save(gallery);
            SuccessResponse response = SuccessResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .timestamp(Instant.now())
                    .message("Gallery updated successfully")
                    .build();
            return response;
        } else {
            SuccessResponse response = SuccessResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .timestamp(Instant.now())
                    .message("Gallery not found")
                    .build();
            return response;
        }
    }

    @Override
    public SuccessResponse softDeleteGallery(Long id) {
        Optional<Gallery> galleryOpt = this.galleryRepo.findByIdAndDeleteAtIsNull(id);
        if (galleryOpt.isPresent()) {
            Gallery gallery = galleryOpt.get();
            gallery.setDeleteAt(java.time.LocalDateTime.now());
            this.galleryRepo.save(gallery);
            SuccessResponse response = SuccessResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .timestamp(Instant.now())
                    .message("Gallery soft deleted successfully")
                    .build();
            return response;
        } else {
            SuccessResponse response = SuccessResponse.builder()
                    .success(false)
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(404)
                    .timestamp(Instant.now())
                    .message("Gallery not found")
                    .build();
            return response;
        }
    }

    @Override
    public SuccessResponse deleteGallery(Long id) {
        this.galleryRepo.deleteById(id);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .message("Gallery hard deleted successfully")
                .build();
        return response;
    }

    @Override
    public DataResponse createGallery(Long catid,List<MultipartFile> images) {
        // Upload each image and create a Gallery entity for each
        List<Gallery> savedGalleries = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl = imageUploader.imageUploader(image);
            Gallery gallery = new Gallery();
            gallery.setImageUrl(imageUrl);
            gallery.setCatid(catid);
            // Optionally set title/detail if needed, or leave null
            savedGalleries.add(galleryRepo.save(gallery));
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .data(savedGalleries)
                .message("Gallery images added successfully")
                .build();
        return response;
    }

    @Override
    public DataResponse createMedia(List<MultipartFile> images) {
        // Upload each image and create a Gallery entity for each
        List<Media> savedMedia = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl = imageUploader.imageUploader(image);
            Media media = new Media();
            media.setImageUrl(imageUrl);
            // Optionally set title/detail if needed, or leave null
            savedMedia.add(mediaRepository.save(media));
        }
        DataResponse response = DataResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .statusCode(200)
                .timestamp(Instant.now())
                .data(savedMedia)
                .message("Media images added successfully")
                .build();
        return response;
    }

	@Override
	public DataResponse deleteMedia(Long id) {
		
		
	    Optional<Media> media = mediaRepository.findById(id);
	    
if(media.isEmpty()) {
	return new DataResponse(false, null, 200, "Media not found", id, null, media, null, null);
}
	    mediaRepository.deleteById(id);

	    return new DataResponse(true, null, 200, "Media deleted successfully", id, null, null, null, null);
	}

	
	 public Map<String, Object> getAllMedia(int page, int size) {
	        Pageable paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
	        Page<Media> pagedResult = mediaRepository.findAll(paging);
	        List<Media> mediaList = pagedResult.getContent();

	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "Media fetched successfully");
	        response.put("statusCode", 200);
	        response.put("data", mediaList);
	        response.put("totalCount", pagedResult.getTotalElements());
	        response.put("totalPages", pagedResult.getTotalPages());
	        response.put("currentPage", page);

	        return response;
	    }

	 @Override
	 public ResponseEntity<?> findByCatid(Long catid, int page, int size) {
	     Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

	     Page<Gallery> galleryPage = galleryRepo.findByCatidAndDeleteAtIsNull(catid, pageable);

	     Map<String, Object> response = new HashMap<>();
	     response.put("content", galleryPage.getContent());
	     response.put("currentPage", galleryPage.getNumber());
	     response.put("totalItems", galleryPage.getTotalElements());
	     response.put("totalPages", galleryPage.getTotalPages());

	     return ResponseEntity.ok(response);
	 }

	@Override
	public ResponseEntity<?> getAllByCatIdwithoupagination(Long catid) {
		// TODO Auto-generated method stub
		return galleryRepo.findByCatid(catid);
	}

	
	
	@Override
	public ResponseEntity<?> findByCidWithoutpagination(Long cid) {
	    try {
	        List<Gallery> galleries = galleryRepo.findByCatidAndDeleteAtIsNull(cid);
	        return ResponseEntity.ok().body(Map.of(
	            "success", true,
	            "data", galleries
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
	            "success", false,
	            "message", e.getMessage()
	        ));
	    }
	}

	

	
	@Override
	public DataResponse updateMedia(List<MultipartFile> images, Long id) {
	    Optional<Media> existingMediaOptional = mediaRepository.findById(id);

	    if (existingMediaOptional.isEmpty()) {
	        return DataResponse.builder()
	                .success(false)
	                .status(HttpStatus.NOT_FOUND)
	                .statusCode(404)
	                .timestamp(Instant.now())
	                .message("Media with ID " + id + " not found")
	                .build();
	    }

	    Media existingMedia = existingMediaOptional.get();

	    // Option 1: Replace all images with new ones (assuming 1:1 mapping)
	    List<Media> updatedMediaList = new ArrayList<>();
	    for (MultipartFile image : images) {
	        String imageUrl = imageUploader.imageUploader(image);

	        // Update existing media or create new based on logic
	        Media updatedMedia = new Media();
	        updatedMedia.setId(existingMedia.getId()); // reuse the existing ID
	        updatedMedia.setImageUrl(imageUrl);
	        // You can also set title/details if required

	        updatedMediaList.add(mediaRepository.save(updatedMedia));
	    }

	    return DataResponse.builder()
	            .success(true)
	            .status(HttpStatus.OK)
	            .statusCode(200)
	            .timestamp(Instant.now())
	            .data(updatedMediaList)
	            .message("Media updated successfully")
	            .build();
	}



}
