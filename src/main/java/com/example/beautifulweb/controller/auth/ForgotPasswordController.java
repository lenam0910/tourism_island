package com.example.beautifulweb.controller.auth;

import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.EmailService;
import com.example.beautifulweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email not found.");
            return "forgot-password";
        }

        String token = userService.generateResetToken(user);
        String resetLink = "http://localhost:8080/reset-password?token=" + token;

        String subject = "Password Reset Request - Côn Đảo Wanderlust";
        String body = "Dear " + user.getUsername() + ",\n\n" +
                "You have requested to reset your password. Please click the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\nThe Côn Đảo Wanderlust Team";
        emailService.sendEmail(user.getEmail(), subject, body);

        model.addAttribute("success", "A password reset link has been sent to your email.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        User user = userService.findByResetToken(token);
        if (user == null || user.getResetTokenExpiry().isBefore(java.time.LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        User user = userService.findByResetToken(token);
        if (user == null || user.getResetTokenExpiry().isBefore(java.time.LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token.");
            return "reset-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        try {
            userService.resetPassword(user, password);
            model.addAttribute("success", "Password reset successfully! Please login with your new password.");
        } catch (Exception e) {
            model.addAttribute("error", "Error resetting password: " + e.getMessage());
        }

        return "reset-password";
    }
}