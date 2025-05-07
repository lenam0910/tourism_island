package com.example.beautifulweb.controller.map;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.repository.TourismRepository;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.ToursimImageService;

@RestController
public class TourismRestController {
    private TourismService tourismService;
    private final TourismRepository tourismRepository;
    private final ToursimImageService tourismImageService;

    public TourismRestController(TourismService tourismService, TourismRepository tourismRepository, ToursimImageService tourismImageService) {
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

    // Xóa
    @DeleteMapping("/api/tourism/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!tourismRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tourismRepository.deleteById(id);
        return ResponseEntity.ok().build();
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

    // Cập nhật
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

    // Thêm mới
    @PostMapping("/api/tourism/add")
    public Tourism addNew(@ModelAttribute Tourism tourism) {
        return tourismRepository.save(tourism);
    }
}
