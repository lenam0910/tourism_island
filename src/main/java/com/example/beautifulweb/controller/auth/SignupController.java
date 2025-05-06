package com.example.beautifulweb.controller.auth;

import com.example.beautifulweb.model.User;
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

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // Trả về file signup.html
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("user") User user, Model model) {
        // Kiểm tra xem email hoặc username đã tồn tại chưa
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email already exists.");
            return "signup";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists.");
            return "signup";
        }

        // Lưu người dùng vào DB
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            model.addAttribute("error", "Error saving user: " + e.getMessage());
            return "signup";
        }

        return "redirect:/login?success"; // Chuyển hướng về trang login với thông báo thành công
    }
}