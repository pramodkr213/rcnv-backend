package com.main.jobilitybackend.services.impl;



import com.main.jobilitybackend.entities.Media;
import com.main.jobilitybackend.repositories.MediaRepository;
import com.main.jobilitybackend.service.MediaService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class MediaServiceImpl implements MediaService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final MediaRepository mediaRepository;

    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Media upload(MultipartFile file) {
        try {
            // Ensure upload folder exists
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);

            // Save file to disk
            file.transferTo(filePath.toFile());

            // Save media info to DB
            Media media = new Media();
            media.setImageUrl(filename);
           
            return mediaRepository.save(media);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public Page<Media> getAll(Pageable pageable) {
        return mediaRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        // Delete the file from disk
        try {
            Path filePath = Paths.get(uploadDir).resolve(media.getImageUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from disk", e);
        }
        mediaRepository.deleteById(id);
    }
}

