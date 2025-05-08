package com.example.beautifulweb.controller.map;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.repository.TourismRepository;
import com.example.beautifulweb.service.FileService;
import com.example.beautifulweb.service.ToursimImageService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class TourismController {
    private final TourismRepository tourismRepository;
    private final FileService fileService;
    private final ToursimImageService tourismImageService;

    public TourismController(TourismRepository tourismRepository, FileService fileService,
            ToursimImageService tourismImageService) {
        this.tourismImageService = tourismImageService;
        this.fileService = fileService;
        this.tourismRepository = tourismRepository;
    }

    @PostMapping("/add")
    public String addTourism(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String packages,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double price,
            @RequestParam("imageLocation") List<MultipartFile> imageLocation) {

        Tourism tourism = new Tourism();
        tourism.setName(name);
        tourism.setDescription(description);
        tourism.setLatitude(latitude);
        tourism.setLongitude(longitude);
        tourism.setPackages(packages);
        tourism.setPrice(price);
        tourism = tourismRepository.save(tourism);

        if (imageLocation != null && !imageLocation.isEmpty()) {
            for (MultipartFile file : imageLocation) {
                if (!file.isEmpty()) {
                    // Lưu file
                    String fileName = fileService.handleSaveUploadFile(file, "tourism", "image");
                    if (fileName != null && !fileName.isEmpty()) {
                        TourismImage tourismImage = new TourismImage();
                        // Đường dẫn để hiển thị qua HTTP (đã cấu hình trong WebConfig)
                        tourismImage.setImagePath("/resources/images/tourism/" + fileName);
                        tourismImage.setTourism(tourism);
                        tourismImageService.saveTourismImage(tourismImage);
                        tourism.getImages().add(tourismImage);
                    }
                }
            }
            tourismRepository.save(tourism);
        }

        return "redirect:/admin/map?add-success";
    }

}
