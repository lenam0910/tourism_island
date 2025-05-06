package com.example.beautifulweb.controller.auth;

import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.EmailService;
import com.example.beautifulweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("user") User user, Model model) {
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email already exists.");
            return "signup";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists.");
            return "signup";
        }

        try {
            userService.saveUser(user);
            String subject = "Welcome to Côn Đảo Wanderlust!";
            String body = "Dear " + user.getUsername() + ",\n\n" +
                    "Thank you for registering with Côn Đảo Wanderlust! Your account has been successfully created.\n" +
                    "You can now log in using your username: " + user.getUsername() + "\n\n" +
                    "Best regards,\nThe Côn Đảo Wanderlust Team";
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            model.addAttribute("error", "Error saving user: " + e.getMessage());
            return "signup";
        }

        return "redirect:/login?success";
    }
}