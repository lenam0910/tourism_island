package com.example.beautifulweb.controller.map;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.service.TourismService;

@RestController
public class TourismRestController {
    private TourismService tourismService;

    public TourismRestController(TourismService tourismService) {
        this.tourismService = tourismService;
    }


    @GetMapping("/api/tourism/all")
    public List<Tourism> getAll() {
        return tourismService.getAll();
    }

}
