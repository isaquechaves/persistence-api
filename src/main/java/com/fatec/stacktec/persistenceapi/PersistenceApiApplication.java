package com.fatec.stacktec.persistenceapi;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.ContextConfiguration;

import com.fatec.stacktec.persistenceapi.configuration.PersistenceApiProperty;
import com.fatec.stacktec.persistenceapi.util.KeycloakAdminRestTemplate;

import lombok.extern.java.Log;

@Log
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages= {"com.fatec.stacktec"})
@ContextConfiguration(classes = {PersistenceApiApplication.class})
public class PersistenceApiApplication {
	
	private final PersistenceApiProperty persistenceProperty;
	
	public PersistenceApiApplication(PersistenceApiProperty persistenceProperty) {
		this.persistenceProperty = persistenceProperty;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PersistenceApiApplication.class, args);
	}
	
	protected OAuth2ProtectedResourceDetails resource() {
		ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
		resourceDetails.setUsername(persistenceProperty.getOAuthCredentials().getUsername());
		resourceDetails.setClientId(persistenceProperty.getOAuthCredentials().getClientId());
		resourceDetails.setClientSecret(persistenceProperty.getOAuthCredentials().getClientSecret());
		resourceDetails.setPassword(persistenceProperty.getOAuthCredentials().getPassword());
		resourceDetails.setGrantType(persistenceProperty.getOAuthCredentials().getGrantType());
		resourceDetails.setAccessTokenUri(persistenceProperty.getOAuthCredentials().getAuthorizationServerUrl());
		return resourceDetails;
	}
	
	
	@Bean
	@ConditionalOnProperty(name = "persistence.oauth-credentials.enabled", havingValue = "true")
	public KeycloakAdminRestTemplate keycloakAdminRestTemplate() {
		KeycloakAdminRestTemplate restTemplate = new KeycloakAdminRestTemplate(resource(), new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
		restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
		restTemplate.setRetryBadAccessTokens(true);
		restTemplate.getAccessToken();
		return restTemplate;
	}
}
