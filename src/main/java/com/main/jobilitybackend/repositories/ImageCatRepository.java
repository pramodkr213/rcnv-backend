package com.main.jobilitybackend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Imagecategory;

public interface ImageCatRepository extends JpaRepository<Imagecategory, Long> {

	Optional<Imagecategory> findByImgcatnameIgnoreCase(String imgcatname);
	
	  Page<Imagecategory> findAll(Pageable pageable);

}
