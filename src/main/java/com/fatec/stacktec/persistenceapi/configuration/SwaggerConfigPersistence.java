package com.fatec.stacktec.persistenceapi.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Sets;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigPersistence {
	
	@Bean
	public Docket api10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("stacktec-persistenceapi-1.0")
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()))
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.fatec.stacktec.persistenceapi.controller"))
					.paths(PathSelectors.any())					
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("StackTec persistence API").description("Documentation API v1.0").build())
				.consumes(Sets.newHashSet("multipart/form-data", "application/json"))
		        .produces(Sets.newHashSet("application/json"));				
	}
	
	  @Bean
	    public SecurityConfiguration securityConfiguration() {
	        return SecurityConfigurationBuilder.builder()
		        		.clientId(null)
		                .clientSecret(null)
		                .scopeSeparator(",")
		                .additionalQueryStringParams(null)
		                .useBasicAuthenticationWithAccessCodeGrant(false)
		                .build();	        	             
	    }

	    @Bean
	    public SecurityScheme securityScheme() {
	    	return new ApiKey("bezkoder", "bezkoder", "cookie");
	    }
	    
	    private SecurityContext securityContext() {
	        return SecurityContext.builder()
	        		.securityReferences(Arrays.asList(new SecurityReference("bezkoder", new AuthorizationScope[0])))
	                .forPaths(PathSelectors.ant("/api/**"))
	                .build();	        	        
	    }

	    List<SecurityReference> defaultAuth() {
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[0];
	        SecurityReference securityReference = new SecurityReference("JWT", authorizationScopes);
	        return Arrays.asList(securityReference);
	    }
}