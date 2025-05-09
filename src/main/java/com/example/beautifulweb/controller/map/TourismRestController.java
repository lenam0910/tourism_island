package com.example.beautifulweb.controller.map;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.repository.TourismRepository;
import com.example.beautifulweb.service.FileService;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.ToursimImageService;

@RestController
public class TourismRestController {
    private TourismService tourismService;
    private final TourismRepository tourismRepository;
    private final ToursimImageService tourismImageService;
    private final FileService fileService;

    public TourismRestController(TourismService tourismService, TourismRepository tourismRepository,
            ToursimImageService tourismImageService, FileService fileService) {
        this.fileService = fileService;
        this.tourismImageService = tourismImageService;
        this.tourismRepository = tourismRepository;
        this.tourismService = tourismService;
    }

    @GetMapping("/api/tourism/all")
    public List<Tourism> getAll() {
        return tourismService.getAll();
    }

    @GetMapping("/api/tourism/images/{id}")
    public ResponseEntity<List<TourismImage>> getTourismImageById(@PathVariable Long id) {
        try {
            List<TourismImage> images = tourismImageService.getImagesByTourismId(id);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/api/tourism/{id}/images")
    public ResponseEntity<Set<TourismImage>> getTourismImages(@PathVariable Long id) {
        try {
            Optional<Tourism> tourism = tourismRepository.findById(id);
            if (tourism.isPresent()) {
                return ResponseEntity.ok(tourism.get().getImages());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/api/tourism/update/{id}")
    public ResponseEntity<Tourism> update(@PathVariable Long id, @ModelAttribute Tourism updated) {
        return tourismRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setDescription(updated.getDescription());
                    existing.setLatitude(updated.getLatitude());
                    existing.setLongitude(updated.getLongitude());
                    tourismRepository.save(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/tourism/add")
    public Tourism addNew(@ModelAttribute Tourism tourism) {
        return tourismRepository.save(tourism);
    }

    @GetMapping("/api/tourism/delete/{tourismId}")
    public ResponseEntity<Void> deleteTourism(@PathVariable Long tourismId) {
        Optional<Tourism> tourism = tourismRepository.findById(tourismId);
        List<TourismImage> images = tourismImageService.getImagesByTourismId(tourismId);
        if (tourism.isPresent()) {
            if (images != null && !images.isEmpty()) {
                for (TourismImage image : images) {
                    String fullPath = image.getImagePath(); // Ví dụ: /uploads/images/tourism/xxx.jpg
                    String fileName = Paths.get(fullPath).getFileName().toString(); // Lấy ra chỉ tên file
                    fileService.handleDeleteImage(fileName, "tourism");
                }
            }

            tourismImageService.deleteImagesByTourismId(tourismId);
            tourismRepository.delete(tourism.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/tourism/nearby")
    public List<Tourism> getNearbyServices(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("radius") double radius) {
        List<Tourism> allTourisms = tourismService.getAll();
        return allTourisms.stream()
                .filter(tourism -> {
                    double distance = calculateDistance(lat, lng, tourism.getLatitude(), tourism.getLongitude());
                    return distance <= radius / 1000; // Chuyển mét thành km
                })
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính Trái Đất (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}