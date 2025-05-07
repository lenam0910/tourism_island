package com.example.beautifulweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendVerificationCodeEmail(String toEmail, String username, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Tiêu đề email
            String subject = "🔐 Yêu cầu đặt lại mật khẩu - Mã xác thực của bạn";

            // Nội dung email HTML
            String body = "<!DOCTYPE html>"
                    + "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Khôi phục mật khẩu</title></head>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>"
                    + "<div style='width: 100%; max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center;'>"
                    + "<div style='background: #007bff; color: white; padding: 15px; font-size: 20px; border-top-left-radius: 10px; border-top-right-radius: 10px;'>"
                    + "🔐 Yêu cầu đặt lại mật khẩu"
                    + "</div>"
                    + "<div style='padding: 20px; color: #333; text-align: left;'>"
                    + "<p>Xin chào " + username + ",</p>"
                    + "<p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình. Vui lòng sử dụng mã xác thực dưới đây để tiếp tục quá trình đặt lại mật khẩu:</p>"
                    + "<div style='text-align: center; margin: 20px 0; font-size: 24px; font-weight: bold; color: #007bff; padding: 10px; border: 2px dashed #007bff; display: inline-block;'>"
                    + otpCode + "</div>"
                    + "<p>Mã này sẽ hết hạn sau 10 phút.</p>"
                    + "<p>Để trở về đăng nhập, hãy nhấn vào nút bên dưới:</p>"
                    + "<div style='text-align: center;'>"
                    + "<a href='http://localhost:8080/login' style='display: inline-block; background: #007bff; color: white; padding: 10px 20px; text-decoration: none; font-size: 16px; border-radius: 5px;'>Quay Về Đăng Nhập</a>"
                    + "</div>"
                    + "<p>Trân trọng,<br>Đội ngũ Côn Đảo Wanderlust</p>"
                    + "</div>"
                    + "<div style='margin-top: 20px; padding: 10px; font-size: 14px; color: #666; background: #f4f4f4; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;'>"
                    + "© 2025 Côn Đảo Wanderlust. Mọi quyền được bảo lưu."
                    + "</div>"
                    + "</div>"
                    + "</body></html>";

            helper.setFrom("jobseeker19886@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            System.out.println("Password reset email sent to: " + toEmail + " successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending password reset email: " + e.getMessage());
        }
    }
}