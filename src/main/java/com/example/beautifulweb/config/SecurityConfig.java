package com.example.beautifulweb.config;

import com.example.beautifulweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private CustomSuccessHandler customSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/resend-code", "/signup", "/forgot-password", "/reset-password",
								"/css/**", "/js/**", "/manager/**")
						.permitAll() // Cho phép truy cập tài nguyên tĩnh trong /manager/**
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.successHandler(customSuccessHandler)
						.failureUrl("/login?error")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID") // Xóa cookie remember-me khi đăng xuất
						.permitAll())
				.rememberMe(rememberMe -> rememberMe // Kích hoạt Remember Me
						.key("uniqueAndSecretKey") // Khóa để mã hóa token
						.tokenValiditySeconds(86400) // Thời gian sống của cookie: 1 ngày (86400 giây)
						.userDetailsService(userService))
				.userDetailsService(userService);

		return http.build();
	}
}