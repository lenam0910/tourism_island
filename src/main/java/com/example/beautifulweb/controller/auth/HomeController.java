package com.example.beautifulweb.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    private final UserService userService;

    public HomeController(UserService userService) {
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

        model.addAttribute("tourBooking", tourBooking);
        return "index";
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {

        return "redirect:/home"; // Redirect to the home page
    }
}