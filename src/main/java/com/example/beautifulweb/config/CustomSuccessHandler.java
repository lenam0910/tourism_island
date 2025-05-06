// package com.example.beautifulweb.config;

// import java.io.IOException;
// import java.util.Collection;
// import java.util.HashMap;
// import java.util.Map;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.web.DefaultRedirectStrategy;
// import org.springframework.security.web.RedirectStrategy;
// import org.springframework.security.web.WebAttributes;
// import
// org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// import com.example.beautifulweb.domain.User;
// import com.example.beautifulweb.service.UserService;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

// @Configuration
// public class CustomSuccessHandler implements AuthenticationSuccessHandler {

// private final UserService userService;
// private static final Logger logger =
// LoggerFactory.getLogger(CustomSuccessHandler.class);

// public CustomSuccessHandler(UserService userService) {
// this.userService = userService;
// }

// protected String determineTargetUrl(final Authentication authentication) {
// Map<String, String> roleTargetUrlMap = new HashMap<>();
// roleTargetUrlMap.put("ROLE_Seeker", "/");
// roleTargetUrlMap.put("ROLE_Admin", "/admin/dashboard");
// roleTargetUrlMap.put("ROLE_Recruiter", "/recruiter/dashboard");

// final Collection<? extends GrantedAuthority> authorities =
// authentication.getAuthorities();
// for (final GrantedAuthority grantedAuthority : authorities) {
// String authorityName = grantedAuthority.getAuthority();
// if (roleTargetUrlMap.containsKey(authorityName)) {
// return roleTargetUrlMap.get(authorityName);
// }
// }
// throw new IllegalStateException();
// }

// protected void clearAuthenticationAttributes(HttpSession session) {
// if (session == null) {
// return;
// }
// session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
// }

// @Override
// public void onAuthenticationSuccess(HttpServletRequest request,
// HttpServletResponse response,
// Authentication authentication) throws IOException, ServletException {
// RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
// HttpSession session = request.getSession(false);
// String targetUrl = determineTargetUrl(authentication);
// if (response.isCommitted()) {
// return;
// }
// // Get username from Spring Security Context Holder
// String username = authentication.getName();
// logger.info("User '{}' logged in with authorities: {}", username,
// authentication.getAuthorities());
// // Get user from database
// User user = userService.getUserByUsername(username);
// session.setAttribute("userId", user.getId());
// session.setAttribute("username", user.getUsername());
// session.setAttribute("fullName", user.getFullName());
// session.setAttribute("avatar",
// (user.getAvatar() == null || user.getAvatar().isEmpty()) ? null :
// user.getAvatar());
// session.setAttribute("provider", user.getProvider());
// // For HR only
// if (user.getWorkingCompany() != null) {
// session.setAttribute("companyId", user.getWorkingCompany().getId());
// }
// redirectStrategy.sendRedirect(request, response, targetUrl);
// clearAuthenticationAttributes(session);
// }

// }
