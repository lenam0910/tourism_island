package com.example.beautifulweb.controller.map;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.repository.TourismRepository;
import com.example.beautifulweb.service.TourismService;

@RestController
public class TourismRestController {
    private TourismService tourismService;
    private final TourismRepository tourismRepository;

    public TourismRestController(TourismService tourismService, TourismRepository tourismRepository) {
        this.tourismRepository = tourismRepository;
        this.tourismService = tourismService;
    }

    @GetMapping("/api/tourism/all")
    public List<Tourism> getAll() {
        return tourismService.getAll();
    }

    // Lấy theo ID
    @GetMapping("/api/tourism/{id}")
    public ResponseEntity<Tourism> getById(@PathVariable Long id) {
        return tourismRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
