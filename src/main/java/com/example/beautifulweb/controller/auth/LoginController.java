package com.example.beautifulweb.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.beautifulweb.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {
        String savedUsername = null;
        String savedPassword = null;
        String rememberMe = null;

        // Đọc cookie từ request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("savedUsername".equals(cookie.getName())) {
                    savedUsername = cookie.getValue();
                }
                if ("savedPassword".equals(cookie.getName())) {
                    savedPassword = cookie.getValue();
                }
                if ("rememberMe".equals(cookie.getName())) {
                    rememberMe = cookie.getValue();
                }
            }
        }

        // Thêm giá trị vào model
        model.addAttribute("savedUsername", savedUsername);
        model.addAttribute("savedPassword", savedPassword);
        model.addAttribute("rememberMe", rememberMe);

        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}