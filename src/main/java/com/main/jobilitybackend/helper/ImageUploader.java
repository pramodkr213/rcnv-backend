package com.main.jobilitybackend.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageUploader {

    @Value("${file.upload-dir}")
    private String baseUploadDir;

    // Saves the file to a specific folder inside 'uploads' and returns the stored
    // filename
    public String saveFile(MultipartFile file, String folderName) {
        try {
            String originalFilename = file.getOriginalFilename();
            String cleanFilename = (originalFilename != null) ? StringUtils.cleanPath(originalFilename) : "unknown";
            String fileName = UUID.randomUUID().toString() + "_" + cleanFilename;

            Path uploadPath = Paths.get(baseUploadDir, folderName);
            if (!uploadPath.isAbsolute()) {
                uploadPath = Paths.get(System.getProperty("user.dir"), baseUploadDir, folderName);
            }

            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Server error while uploading file: " + ex.getMessage(), ex);
        }
    }

    // Builds the URL for accessing the uploaded file
    private static String buildFileUrl(String folder, String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(folder + "/")
                .path(filename)
                .toUriString();
    }

    // Upload image and return public URL
    public String imageUploader(MultipartFile file) {
        String filename = saveFile(file, "images");
        return buildFileUrl("images", filename);
    }

    // Upload resume and return public URL
    public String resumeUploader(MultipartFile file) {
        String filename = saveFile(file, "resumes");
        return buildFileUrl("resumes", filename);
    }

    // Upload document and return public URL
    public String documentUploader(MultipartFile file) {
        String filename = saveFile(file, "documents");
        return buildFileUrl("documents", filename);
    }
}
