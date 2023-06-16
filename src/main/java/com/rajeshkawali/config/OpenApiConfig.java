package com.rajeshkawali.config;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;


/**
 * @author Rajesh_Kawali
 * 
 */
@Configuration
public class OpenApiConfig {

	@Bean
	@Profile("!prod")
	public GroupedOpenApi actuatorApi(OpenApiCustomizer actuatorOpenApiCustomiser,
			OperationCustomizer actuatorCustomizer, WebEndpointProperties endpointProperties,
			@Value("${springdoc.version}") String appVersion) {
		return GroupedOpenApi.builder()
				.group("Actuator")
				.pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
				.addOpenApiCustomizer(actuatorOpenApiCustomiser)
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Actuator API").version(appVersion)))
				.addOperationCustomizer(actuatorCustomizer)
				.pathsToExclude("/health/*").build();
	}

	@Bean
	public GroupedOpenApi usersGroup(@Value("${springdoc.version}") String appVersion) {
		return GroupedOpenApi.builder().group("customers").addOperationCustomizer((operation, handlerMethod) -> {
			operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
			return operation;
		}).addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Customer API").version(appVersion)))
				.packagesToScan("com.rajeshkawali").build();
	}
}
