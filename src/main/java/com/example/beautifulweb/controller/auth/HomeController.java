package com.example.beautifulweb.controller.auth;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    private final UserService userService;
    private final TourismService tourismService;

    public HomeController(UserService userService, TourismService tourismService) {
        this.tourismService = tourismService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        TourBooking tourBooking = new TourBooking();

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                tourBooking.setName(user.getFullName());
                tourBooking.setEmail(user.getEmail());
            }
        }
        List<String> destinations = tourismService.getAllDestinations();
        model.addAttribute("tourBooking", tourBooking);
        model.addAttribute("destinations", destinations);
        return "index";
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {

        return "redirect:/home"; // Redirect to the home page
    }
}