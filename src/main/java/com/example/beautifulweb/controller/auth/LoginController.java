package com.example.beautifulweb.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {

        // Check for rememberMePreference cookie
        boolean rememberMe = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMePreference".equals(cookie.getName()) && "true".equals(cookie.getValue())) {
                    rememberMe = true;
                    break;
                }
            }
        }

        model.addAttribute("rememberMe", rememberMe);
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout"; // Redirect về trang đăng nhập với thông báo logout
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Trả về file access-denied.html
    }

    @PostMapping("/login")
    public String signupUser(@ModelAttribute("user") User user, Model model) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser == null) {
            model.addAttribute("error", "Username or Password is not correct!");
            return "login";
        }

        if (!userService.checkPassword(user.getPassword(), existingUser.getPassword())) {
            model.addAttribute("error", "Username or Password is not correct!");
            return "login";
        }
        return "redirect:/login?success";
    }
}