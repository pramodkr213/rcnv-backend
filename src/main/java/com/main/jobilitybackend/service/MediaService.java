package com.main.jobilitybackend.service;



import com.main.jobilitybackend.entities.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    Media upload(MultipartFile file);
    Page<Media> getAll(Pageable pageable);
    void delete(Long id);
}
