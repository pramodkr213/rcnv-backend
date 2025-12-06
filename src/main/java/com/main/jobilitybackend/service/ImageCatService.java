package com.main.jobilitybackend.service;

import org.springframework.http.ResponseEntity;

import com.main.jobilitybackend.entities.Imagecategory;

public interface ImageCatService {

	ResponseEntity<?> save(Imagecategory imgcat);

	 ResponseEntity<?> getAllImgcategory(int page, int size);

	ResponseEntity<?> deleteImgCat(Long id);

	ResponseEntity<?> updateImgCatData(Imagecategory data);

	

}
