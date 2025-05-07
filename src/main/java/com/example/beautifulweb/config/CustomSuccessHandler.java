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

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);

    @Autowired
    private UserService userService;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("Session is null, cannot store user information.");
            redirectStrategy.sendRedirect(request, response, "/");
            return;
        }

        // Get username from Spring Security Authentication
        String username = authentication.getName();
        logger.info("User '{}' logged in with authorities: {}", username, authentication.getAuthorities());

        // Get user from database
        User user = userService.findByUsername(username);
        if (user != null) {
            // Store user information in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            logger.info("Stored in session - userId: {}, username: {}, role: {}", user.getId(), user.getUsername(),
                    user.getRole());
        } else {
            logger.warn("User '{}' not found in database.", username);
        }

        // Check if "Remember Me" was selected and store/remove preference in a cookie
        String rememberMe = request.getParameter("remember-me");
        if ("on".equals(rememberMe)) {
            Cookie rememberMeCookie = new Cookie("rememberMePreference", "true");
            rememberMeCookie.setMaxAge(30 * 24 * 60 * 60); // Cookie sống 30 ngày
            rememberMeCookie.setPath("/");
            response.addCookie(rememberMeCookie);
        } else {
            // If "Remember Me" is not selected, remove the preference cookie
            Cookie rememberMeCookie = new Cookie("rememberMePreference", null);
            rememberMeCookie.setMaxAge(0); // Xóa cookie
            rememberMeCookie.setPath("/");
            response.addCookie(rememberMeCookie);
        }

        // Clear authentication attributes
        clearAuthenticationAttributes(session);

        // Redirect based on role
        String targetUrl = (user != null && "ROLE_ADMIN".equals(user.getRole())) ? "/admin/dashboard" : "/";
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