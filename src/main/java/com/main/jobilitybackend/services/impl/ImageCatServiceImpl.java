package com.main.jobilitybackend.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.entities.Imagecategory;
import com.main.jobilitybackend.repositories.ImageCatRepository;
import com.main.jobilitybackend.service.ImageCatService;

@Service
public class ImageCatServiceImpl implements ImageCatService{
	@Autowired
	private ImageCatRepository imageCatRepository;

	@Override
	public ResponseEntity<?> save(Imagecategory imgcat) {
	    try {
	       
	        String nameToCheck = imgcat.getImgcatname().trim();

	        
	        Optional<Imagecategory> existcat = imageCatRepository.findByImgcatnameIgnoreCase(nameToCheck);
	        if (existcat.isPresent()) {
	            return ResponseEntity
	                .badRequest()
	                .body("Category '" + nameToCheck + "' already exists.");
	        }

	        // Save new category with trimmed name (you can also convert to lowercase/capitalized if you want uniform storage)
	        imgcat.setImgcatname(nameToCheck); // optional: normalize before saving
	        Imagecategory savedCat = imageCatRepository.save(imgcat);

	        return ResponseEntity.ok(savedCat);


	    } catch (Exception e) {
	        return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Something went wrong while saving the category: " + e.getMessage());
	    }
	}

	   @Override
	    public ResponseEntity<?> getAllImgcategory(int page, int size) {
	        try {
	            // Validate parameters
	            if (page < 0) {
	                return ResponseEntity.badRequest().body("Page number cannot be negative");
	            }
	            if (size <= 0) {
	                return ResponseEntity.badRequest().body("Page size must be greater than 0");
	            }

	            // Create pagination request
	            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

	            // Get paginated results
	            Page<Imagecategory> categoryPage = imageCatRepository.findAll(pageable);

	            // Check if page exists
	            if (page > 0 && page >= categoryPage.getTotalPages()) {
	                return ResponseEntity.badRequest().body("Requested page exceeds total pages");
	            }

	            // Build response
	            Map<String, Object> response = new HashMap<>();
	            response.put("content", categoryPage.getContent());
	            response.put("currentPage", categoryPage.getNumber());
	            response.put("totalItems", categoryPage.getTotalElements());
	            response.put("totalPages", categoryPage.getTotalPages());
	            response.put("hasNext", categoryPage.hasNext());
	            response.put("hasPrevious", categoryPage.hasPrevious());

	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError()
	                    .body("Error occurred while fetching image categories: " + e.getMessage());
	        }
	    }

	@Override
	public ResponseEntity<?> deleteImgCat(Long id) {
		
		
	Optional<Imagecategory> existdata = 	imageCatRepository.findById(id);
	
	if(existdata.isEmpty()) {
		return ResponseEntity.notFound().build();
	}
		
	imageCatRepository.deleteById(id);
	
		return ResponseEntity.ok().body("Image Category Deleted..");
	}

	@Override
	public ResponseEntity<?> updateImgCatData(Imagecategory data) {


		Optional<Imagecategory> existdata =  imageCatRepository.findById(data.getId());
		
		if(existdata.isEmpty()) {
			ResponseEntity.badRequest().body("Category Not Found With This Id");
			
		}
		
		
		Imagecategory ee = existdata.get();
		
		ee.setImgcatname(data.getImgcatname());
		
	Imagecategory eee = 	imageCatRepository.save(ee);
		
		return ResponseEntity.ok(eee) ;
	}




}
