package com.mickey.cinebook.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
	@Bean
	public OpenAPI cinebookOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						      .title("CineBook API")
						      .description("Movie theatre booking and reservation backend")
						      .version("1.0.0")
						      .contact(new Contact()
								               .name("Mickey")
						              )
				     )
				.components(new Components()
						            .addSecuritySchemes(
								            "bearerAuth",
								            new SecurityScheme()
										            .name("Authorization")
										            .type(SecurityScheme.Type.HTTP)
										            .scheme("bearer")
										            .bearerFormat("JWT")
						                               )
				           )
				.addSecurityItem(
						new SecurityRequirement().addList("bearerAuth")
				                )
				;
	}
}
