package com.example.beautifulweb.config;

import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    private final AppConfig appConfig = new AppConfig();

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        }

        String username = authentication.getName();
        String plainPassword = (String) request.getSession().getAttribute("tempPlainPassword");
        String password = request.getParameter("password");

        // Xử lý cookie
        if ("on".equals(request.getParameter("remember-me"))) {
            // Lưu trạng thái "Remember Me" vào cookie
            Cookie rememberMeCookie = new Cookie("rememberMe", "true");
            rememberMeCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
            rememberMeCookie.setPath("/");
            rememberMeCookie.setHttpOnly(true);
            rememberMeCookie.setSecure(true);
            response.addCookie(rememberMeCookie);

            // Lưu username vào cookie
            Cookie usernameCookie = new Cookie("savedUsername", username);
            usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
            usernameCookie.setPath("/");
            usernameCookie.setHttpOnly(true);
            usernameCookie.setSecure(true);
            response.addCookie(usernameCookie);

            // Lưu password vào cookie (mã hóa Base64)
            User user = userService.findByUsername(username);
            if (user != null && user.getPassword() != null) {
                if (appConfig.passwordEncoder().matches(password, user.getPassword())) {
                    Cookie passwordCookie = new Cookie("savedPassword", password);
                    passwordCookie.setMaxAge(30 * 24 * 60 * 60);
                    passwordCookie.setPath("/");
                    passwordCookie.setHttpOnly(true);
                    passwordCookie.setSecure(true);
                    response.addCookie(passwordCookie);
                }

            }
        } else {
            // Xóa các cookie nếu không chọn "Remember Me"
            Cookie rememberMeCookie = new Cookie("rememberMe", "");
            rememberMeCookie.setMaxAge(0); // Xóa cookie
            rememberMeCookie.setPath("/");
            response.addCookie(rememberMeCookie);

            Cookie usernameCookie = new Cookie("savedUsername", "");
            usernameCookie.setMaxAge(0); // Xóa cookie
            usernameCookie.setPath("/");
            response.addCookie(usernameCookie);

            Cookie passwordCookie = new Cookie("savedPassword", "");
            passwordCookie.setMaxAge(0); // Xóa cookie
            passwordCookie.setPath("/");
            response.addCookie(passwordCookie);
        }

        User user = userService.findByUsername(username);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

        } else {
        }

        clearAuthenticationAttributes(session);

        String targetUrl;
        if (user != null && "ADMIN".equals(user.getRole())) {
            targetUrl = "/admin/map?login-success";
        } else {
            targetUrl = "/?login-success";
        }

        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }
    }

    protected void clearAuthenticationAttributes(HttpSession session) {
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}