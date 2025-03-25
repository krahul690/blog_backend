package com.blog.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info().title("Blog Management System API").description("API documentation for Blog system")
						.version("1.0").contact(new Contact().name("Rahul Paswan").email("rahul@example.com")));
	}
}
