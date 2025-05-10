package com.example.beautifulweb.controller.map;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.model.User;
import com.example.beautifulweb.repository.TourismRepository;
import com.example.beautifulweb.service.EmailService;
import com.example.beautifulweb.service.FileService;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.ToursimImageService;
import com.example.beautifulweb.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class TourismController {
    private final TourismRepository tourismRepository;
    private final FileService fileService;
    private final ToursimImageService tourismImageService;
    private final TourismService tourismService;
    private final EmailService emailService;
    private final UserService userService;

    public TourismController(TourismRepository tourismRepository, FileService fileService,
            ToursimImageService tourismImageService, TourismService tourismService, EmailService emailService,
            UserService userService) {
        this.userService = userService;
        this.emailService = emailService;
        this.tourismService = tourismService;
        this.tourismImageService = tourismImageService;
        this.fileService = fileService;
        this.tourismRepository = tourismRepository;
    }

    @GetMapping("/book-tour")
    public String bookTour(@Valid @ModelAttribute("tourBooking") TourBooking tourBooking,
            BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (result.hasErrors()) {
            // Nếu có lỗi validate, trả lại trang với form và lỗi
            model.addAttribute("tourBooking", new TourBooking());

            return "tour-user-detail"; // Trả lại template để hiển thị lỗi
        }
        User user = userService.getUserById((Long) session.getAttribute("userId"));
        if (user == null) {
            return "redirect:/login";
        }
        emailService.sendBookingConfirmationEmail(null, null, null, null);
        return "redirect:/tourism-details" + "?success-booking";
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
            existingTourism.setPackages(tourism.getPackages());
            existingTourism.setTimeline(tourism.getTimeline());
            existingTourism.setPrice(tourism.getPrice());

            // Lưu đối tượng Tourism đã cập nhật vào cơ sở dữ liệu
            tourismRepository.save(existingTourism);
        }
        if (imageLocation != null && !imageLocation.isEmpty()) {
            // Lọc các file thực sự được upload (không rỗng)
            List<MultipartFile> validFiles = imageLocation.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .toList();

            if (!validFiles.isEmpty()) {
                // Xóa ảnh cũ
                List<TourismImage> images = tourismImageService.getImagesByTourismId(tourismId);
                if (images != null && !images.isEmpty()) {
                    tourismImageService.deleteImagesByTourismId(tourismId);
                    for (TourismImage image : images) {
                        String fullPath = image.getImagePath();
                        String fileName = Paths.get(fullPath).getFileName().toString();
                        fileService.handleDeleteImage(fileName, "tourism");
                    }
                }
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
        }
        return "redirect:/admin/tourism-manage/view/" + tourismId + "?update-success";

    }

    @PostMapping("/add")
    public String addTourism(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String packages,
            @RequestParam String timeline,
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
        tourism.setTimeline(timeline);
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
