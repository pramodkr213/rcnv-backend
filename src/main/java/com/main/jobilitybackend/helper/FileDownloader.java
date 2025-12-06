package com.main.jobilitybackend.helper;

import java.net.MalformedURLException;
import java.nio.file.Files;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDownloader {
    public ResponseEntity<Resource> downloadImage(String filename) {
        return download("images", filename);
    }

    public ResponseEntity<Resource> downloadResume(String filename) {
        return download("resumes", filename);
    }

    public ResponseEntity<Resource> downloadDocument(String filename) {
        return download("documents", filename);
    }

    private ResponseEntity<Resource> download(String folder, String filename) {
        try {
            filename = filename.replace("..", ""); // prevent path traversal
            Path filePath = Paths.get("uploads", folder).resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + filename, e);
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file: " + filename, e);
        }
    }

    
}
