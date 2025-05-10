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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;

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
    public String processForgotPassword(@RequestParam("username") String username, @RequestParam("email") String email,
            Model model) {
        // Kiểm tra cả username và email
        User user = userService.findByUsername(username);
        if (user == null || !user.getEmail().equalsIgnoreCase(email)) {
            model.addAttribute("error", "Username or email not found.");
            return "forgot-password";
        }

        // Tạo mã xác thực ngẫu nhiên (6 chữ số)
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        // Mã hóa mã xác thực bằng SHA256
        String hashedCode = hashCode(verificationCode);

        user.setResetToken(hashedCode);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10)); // Hết hạn sau 10 phút
        userService.saveUser(user);

        emailService.sendVerificationCodeEmail(user.getEmail(), user.getUsername(), verificationCode);

        // Chuyển hướng đến trang nhập mã xác thực
        model.addAttribute("userId", user.getId());
        model.addAttribute("hashedCode", hashedCode);
        return "reset-password";
    }

    @GetMapping("/resend-code")
    public String resendCode(@RequestParam("userId") Long userId, Model model) {
        User user = userService.findById(userId);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "forgot-password";
        }

        // Tạo mã xác thực mới
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        String hashedCode = hashCode(verificationCode);

        // Cập nhật mã xác thực và thời gian hết hạn
        user.setResetToken(hashedCode);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10));
        userService.saveUser(user);

        emailService.sendVerificationCodeEmail(user.getEmail(), user.getUsername(), verificationCode);

        // Trả về trang nhập mã xác thực
        model.addAttribute("userId", user.getId());
        model.addAttribute("hashedCode", hashedCode);
        return "reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("userId") Long userId,
            @RequestParam("hashedCode") String hashedCode, Model model) {
        User user = userService.findById(userId);
        if (user == null || !user.getResetToken().equals(hashedCode)) {
            model.addAttribute("error", "Invalid or expired verification code.");
            return "forgot-password";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("hashedCode", hashedCode);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("id") Long userId,
            @RequestParam("verificationCode") String verificationCode,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        User user = userService.findById(userId);
        if (user == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired verification code.");
            return "forgot-password";
        }

        // Kiểm tra mã xác thực
        String hashedEnteredCode = hashCode(verificationCode);
        if (!hashedEnteredCode.equals(user.getResetToken())) {
            model.addAttribute("error", "Mã xác thực không chính xác. Vui lòng thử lại!");
            model.addAttribute("userId", user.getId());
            model.addAttribute("hashedCode", user.getResetToken());
            return "reset-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("userId", user.getId());
            model.addAttribute("hashedCode", user.getResetToken());
            return "reset-password";
        }

        try {
            userService.resetPassword(user, newPassword);
            model.addAttribute("success", "Password reset successfully! Please login with your new password.");
        } catch (Exception e) {
            model.addAttribute("error", "Error resetting password: " + e.getMessage());
            model.addAttribute("userId", user.getId());
            model.addAttribute("hashedCode", user.getResetToken());
            return "reset-password";
        }

        return "reset-password";
    }

    // Phương thức mã hóa SHA256
    private String hashCode(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(code.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing code", e);
        }
    }
}