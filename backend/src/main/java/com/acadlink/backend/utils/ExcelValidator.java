package com.acadlink.backend.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelValidator {
    
    public void validateFile(MultipartFile file) {

    if (file.isEmpty()) {
        throw new IllegalArgumentException("File is empty");
    }

    String filename = file.getOriginalFilename();

    if (filename == null || !filename.endsWith(".xlsx")) {
        throw new IllegalArgumentException("Only .xlsx files are allowed");
    }

    // Optional size check (ex: 2MB)
    long maxSize = 2 * 1024 * 1024; 
    if (file.getSize() > maxSize) {
        throw new IllegalArgumentException("File size too large");
    }
}

}
