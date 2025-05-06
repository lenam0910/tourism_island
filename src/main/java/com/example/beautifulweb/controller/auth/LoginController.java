package com.example.beautifulweb.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Trả về file login.html
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout"; // Redirect về trang đăng nhập với thông báo logout
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Trả về file access-denied.html
    }
}