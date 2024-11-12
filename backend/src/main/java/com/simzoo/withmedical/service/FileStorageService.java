package com.simzoo.withmedical.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        // 디렉토리(파일경로) 존재 여부 확인
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        log.info("Saving fileName = " + fileName);
        Path filePath = uploadPath.resolve(fileName);
        log.info("Saving filePath =  " + filePath);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    public Path getFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
}
