// package swp391.jobseeker.config;

// import java.io.IOException;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.DisabledException;
// import org.springframework.security.core.AuthenticationException;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import
// org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import
// org.springframework.security.web.authentication.AuthenticationFailureHandler;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Configuration
// public class CustomFailureHandler implements AuthenticationFailureHandler {

// @Override
// public void onAuthenticationFailure(HttpServletRequest request,
// HttpServletResponse response,
// AuthenticationException exception) throws IOException, ServletException {
// String errorMessage = "";
// if (exception.getClass().isAssignableFrom(BadCredentialsException.class)
// || exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
// errorMessage = "invalid";
// } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
// errorMessage = "disabled";
// } else if
// (exception.getClass().isAssignableFrom(OAuth2AuthenticationException.class))
// {
// errorMessage = "oauth2-error";
// } else {
// errorMessage = "login-error";
// }

// response.sendRedirect(request.getContextPath() + "/login?" + errorMessage);
// }

// }
