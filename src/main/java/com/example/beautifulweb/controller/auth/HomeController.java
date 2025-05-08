package com.example.beautifulweb.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.example.beautifulweb.model.TourBooking;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tourBooking", new TourBooking());
        return "index";
    }
}