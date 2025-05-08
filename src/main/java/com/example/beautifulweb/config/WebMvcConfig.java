package com.example.beautifulweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false); // Disable cache for development
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

	@Value("${upload.folder.images}")
	private String imageUploadPath;

	@SuppressWarnings("null")
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/images/**")
				.addResourceLocations("file:" + imageUploadPath + "/");

		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/");

		registry
				.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/");

		registry
				.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/static/img/");

		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/static/js/");

		registry.addResourceHandler("/lib/**")
				.addResourceLocations("classpath:/static/lib/");

		registry.addResourceHandler("/manager/**")
				.addResourceLocations("classpath:/static/manager/");

		registry.addResourceHandler("/user/**")
				.addResourceLocations("classpath:/static/user/");
	}
}