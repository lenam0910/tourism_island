package com.example.beautifulweb.controller.map;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.repository.TourismRepository;
import com.example.beautifulweb.service.FileService;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.ToursimImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
public class TourismController {
    private final TourismRepository tourismRepository;
    private final FileService fileService;
    private final ToursimImageService tourismImageService;
    private final TourismService tourismService;

    public TourismController(TourismRepository tourismRepository, FileService fileService,
            ToursimImageService tourismImageService, TourismService tourismService) {
        this.tourismService = tourismService;
        this.tourismImageService = tourismImageService;
        this.fileService = fileService;
        this.tourismRepository = tourismRepository;
    }

    @GetMapping("admin/tourism-manage/view/{tourismId}")
    public String viewTourism(@PathVariable Long tourismId, Model model) {
        // Tìm kiếm đối tượng Tourism theo ID
        Tourism tourisms = tourismService.getById(tourismId);
        List<TourismImage> images = tourismImageService.getImagesByTourismId(tourismId);
        model.addAttribute("tourism", tourisms); // Thêm đối tượng Tourism vào model
        model.addAttribute("images", images); // Thêm danh sách hình ảnh vào model
        return "admin/tourism-detail"; // Trả về view hiển thị thông tin chi tiết

    }

    @GetMapping("admin/tourism-manage/edit/{tourismId}")
    public String editTourism(@PathVariable Long tourismId, Model model) {
        Tourism tourism = tourismService.getById(tourismId);
        model.addAttribute("tourism", tourism);
        List<TourismImage> images = tourismImageService.getImagesByTourismId(tourismId);
        model.addAttribute("images", images); // Thêm danh sách hình ảnh vào model
        return "admin/tourism-edit";
    }

    @PostMapping("/update/{tourismId}")
    public String updateTourism(@PathVariable Long tourismId, @ModelAttribute("tourism") Tourism tourism,
            @RequestParam("imageLocation") List<MultipartFile> imageLocation) {
        // Tìm kiếm đối tượng Tourism theo ID
        Tourism existingTourism = tourismService.getById(tourismId);
        if (existingTourism != null) {
            // Cập nhật thông tin của đối tượng Tourism
            existingTourism.setName(tourism.getName());
            existingTourism.setDescription(tourism.getDescription());
            existingTourism.setLatitude(tourism.getLatitude());
            existingTourism.setLongitude(tourism.getLongitude());
            existingTourism.setPackages(tourism.getPackages());
            existingTourism.setPrice(tourism.getPrice());

            // Lưu đối tượng Tourism đã cập nhật vào cơ sở dữ liệu
            tourismRepository.save(existingTourism);
        }
        List<TourismImage> images = tourismImageService.getImagesByTourismId(tourismId);
        if (images != null && !images.isEmpty()) {
            tourismImageService.deleteImagesByTourismId(tourismId);
            for (TourismImage image : images) {
                String fullPath = image.getImagePath(); // Ví dụ: /uploads/images/tourism/xxx.jpg
                String fileName = Paths.get(fullPath).getFileName().toString(); // Lấy ra chỉ tên file
                fileService.handleDeleteImage(fileName, "tourism");
            }
        }
        if (imageLocation != null && !imageLocation.isEmpty()) {
            for (MultipartFile file : imageLocation) {
                if (!file.isEmpty()) {
                    // Lưu file
                    String fileName = fileService.handleSaveUploadFile(file, "tourism", "image");
                    if (fileName != null && !fileName.isEmpty()) {
                        TourismImage tourismImage = new TourismImage();
                        // Đường dẫn để hiển thị qua HTTP (đã cấu hình trong WebConfig)
                        tourismImage.setImagePath("/uploads/images/tourism/" + fileName);
                        tourismImage.setTourism(existingTourism);
                        tourismImageService.saveTourismImage(tourismImage);
                    }
                }
            }
            tourismRepository.save(existingTourism);
        }

        return "redirect:/admin/tourism-manage/view/" + tourismId + "?update-success";
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
                        tourismImage.setImagePath("/uploads/images/tourism/" + fileName);
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
