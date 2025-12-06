package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Gallery;
import com.main.jobilitybackend.entities.Media;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

public interface MediaRepository extends JpaRepository<Media, Long>{
	  Page<Media> findAll(Pageable pageable);
}
