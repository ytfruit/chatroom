package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUploadService {
    Map<String, Object> upload(MultipartFile file, Long userId, Integer roomId);
    String getPreviewUrl(String fileName, String bucketName);
    Map<String, Object> saveAvactar(MultipartFile file, Long userId);
}
