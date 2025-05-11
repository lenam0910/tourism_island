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
    private String uploadDir;

    /**
     * Lưu file upload vào thư mục cụ thể
     *
     * @param file         file upload
     * @param targetFolder thư mục con (dưới thư mục gốc của ảnh)
     * @param type         loại file: "image"
     * @return tên file đã lưu hoặc null nếu lỗi
     */
    public String handleSaveUploadFile(MultipartFile file, String targetFolder, String type) {
        if (file.isEmpty()) {
            return "";
        }

        // Xác định thư mục lưu file
        String rootPath;
        if ("image".equals(type)) {
            rootPath = uploadDir + "/images";
        } else if ("cv".equals(type)) {
            rootPath = uploadDir + "/cv";
        } else {
            return null;
        }

        String finalName;
        try {
            byte[] bytes = file.getBytes();
            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists()) {
                dir.mkdirs(); // Tạo thư mục nếu chưa có
            }

            // Tạo tên file duy nhất
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);

            // Lưu file
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }
            return finalName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Xóa ảnh từ thư mục ngoài
     */
    public boolean handleDeleteImage(String fileName, String targetFolder) {
        String filePath = uploadDir + "/images/" + targetFolder + "/" + fileName;
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
