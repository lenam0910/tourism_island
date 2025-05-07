package com.example.beautifulweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${upload.folder.images}")
    private String imageUploadPath;

    /**
     * Lưu file upload vào thư mục cụ thể
     *
     * @param file         file upload
     * @param targetFolder thư mục con (dưới thư mục gốc của ảnh)
     * @param type         loại file: "image"
     * @return tên file đã lưu hoặc null nếu lỗi
     */
    public String handleSaveUploadFile(MultipartFile file, String targetFolder, String type) {
        if (file.isEmpty() || !"image".equalsIgnoreCase(type)) {
            return null;
        }

        try {
            String folderPath = imageUploadPath + File.separator + targetFolder;
            Files.createDirectories(Paths.get(folderPath));

            String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(folderPath, finalName);

            Files.write(filePath, file.getBytes());
            return finalName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean handleDeleteImage(String fileName, String targetFolder) {
        try {
            Path path = Paths.get(imageUploadPath, targetFolder, fileName);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
