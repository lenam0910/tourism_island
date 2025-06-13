package com.example.beautifulweb.controller.navigation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.model.Tourism;
import com.example.beautifulweb.model.TourismImage;
import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.ToursimImageService;
import com.example.beautifulweb.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class navigationController {

    private final TourismService tourismService;
    private final ToursimImageService toursimImageService;
    private final UserService userService;

    public navigationController(TourismService tourismService, ToursimImageService toursimImageService,
            UserService userService) {
        this.userService = userService;
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
    @GetMapping("/flight-search")
    public String test() {
        return "flight-search"; // Returns the flight-search.html template
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
    public String tourDetailWtihId(@PathVariable("tourismId") Long tourismId, HttpSession session, Model model) {
        // Lấy thông tin tourism
        Tourism tourism = tourismService.getById(tourismId);
        model.addAttribute("tourism", tourism);

        // Lấy danh sách destinations
        List<String> destinations = tourismService.getAllDestinations();
        model.addAttribute("destinations", destinations);

        // Tạo tourBooking và điền thông tin người dùng
        TourBooking tourBooking = new TourBooking();
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                tourBooking.setName(user.getFullName());
                tourBooking.setEmail(user.getEmail());
            }
        }

        String tourismName = tourism.getName();
        if (tourismName != null) {
            for (String dest : destinations) {
                if (dest.equalsIgnoreCase(tourismName)) {
                    tourBooking.setDestination(dest);
                    break;
                }
            }
        }

        tourBooking.setId(tourismId);
        model.addAttribute("tourBooking", tourBooking);

        // Lấy danh sách images
        List<TourismImage> tourismImages = toursimImageService.getImagesByTourismId(tourismId);
        model.addAttribute("images", tourismImages);

        return "tour-user-detail";
    }

    @GetMapping("/tour-details")
    public String tourDetails(Model model) {

        return "tour-user-detail";
    }
}
