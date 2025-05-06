package com.example.beautifulweb.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotPasswordController {

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password"; // Trả về file forgot-password.html
    }
}