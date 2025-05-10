package com.example.beautifulweb.controller.navigation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.ToursimImageService;

import org.springframework.ui.Model;

@Controller
public class navigationController {

    private final TourismService tourismService;
    private final ToursimImageService toursimImageService;

    public navigationController(TourismService tourismService, ToursimImageService toursimImageService) {
        this.toursimImageService = toursimImageService;
        this.tourismService = tourismService;
    }
    
    // This class is used to handle navigation between pages in the web application.
    // It contains methods that map to different URLs and return the corresponding
    // HTML templates.
    // The methods are annotated with @GetMapping to specify the HTTP GET request
    // method.

    @GetMapping("/about")
    public String about() {
        return "about"; // Returns the about.html template
    }

    @GetMapping("/services")
    public String services() {
        return "service"; // Returns the services.html template
    }

    @GetMapping("/package")
    public String packages() {
        return "package"; // Returns the packages.html template
    }

    @GetMapping("/map")
    public String map() {
        return "map"; // Returns the map.html template
    }

    @GetMapping("/tourism-details/{tourismId}")
    public String tourDetailWtihId(@PathVariable("tourismId") Long tourismId, Model model) {
        Tourism tourism = tourismService.getById(tourismId);
        List<TourismImage> tourismImages = toursimImageService.getImagesByTourismId(tourismId);
        model.addAttribute("tourism", tourism);
        model.addAttribute("images", tourismImages);
        return "tour-user-detail"; 
    }

    @GetMapping("/tour-details")
    public String tourDetails(Model model) {

        return "tour-user-detail"; 
    }
}
