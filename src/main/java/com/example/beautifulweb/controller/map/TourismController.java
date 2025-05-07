package com.example.beautifulweb.controller.map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.repository.TourismRepository;

@Controller
public class TourismController {
    private final TourismRepository tourismRepository;

    public TourismController(TourismRepository tourismRepository) {
        this.tourismRepository = tourismRepository;
    }

    @PostMapping("/add")
    public String addTourism(@RequestParam String name,
            @RequestParam String description,
            @RequestParam double latitude,
            @RequestParam double longitude) {

        Tourism t = new Tourism();
        t.setName(name);
        t.setDescription(description);
        t.setLatitude(latitude);
        t.setLongitude(longitude);
        tourismRepository.save(t);

        return "redirect:/map"; // hoặc trang bạn muốn quay lại
    }
}
