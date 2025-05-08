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
}