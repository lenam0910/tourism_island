package com.example.beautifulweb.controller.auth;

import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.RecaptchaService;
import com.example.beautifulweb.service.UserService;
import com.example.beautifulweb.config.AppConfig;
import com.example.beautifulweb.config.RecaptchaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

@Controller
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private RecaptchaConfig recaptchaConfig;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("recaptchaSiteKey", recaptchaConfig.getSiteKey());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(value = "g-recaptcha-response", required = false) String recaptchaResponse, Model model) {

        // Kiểm tra xem tên đăng nhập hoặc email đã tồn tại chưa
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng.");
            return "redirect:/signup?existed";
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email đã được sử dụng.");
            model.addAttribute("recaptchaSiteKey", recaptchaConfig.getSiteKey());
            return "redirect:/signup?email_existed";
        }
        user.setPassword(appConfig.passwordEncoder().encode(user.getPassword()));
        // Lưu người dùng mới với role USER
        user.setRole("USER");
        userService.saveUser(user);

        return "redirect:/login?success";
    }
}