package com.security.EduNext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableDiscoveryClient
public class EduNextApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduNextApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:4200") // Autoriser le frontend
						.allowedMethods("GET", "POST", "PUT", "DELETE") // Méthodes HTTP autorisées
						.allowedHeaders("*"); // Autoriser tous les headers
			}
		};
	}

}
