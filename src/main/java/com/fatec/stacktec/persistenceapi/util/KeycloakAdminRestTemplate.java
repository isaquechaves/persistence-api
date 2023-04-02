package com.fatec.stacktec.persistenceapi.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@ConditionalOnProperty(name = "persistence.oauth-credentials.enabled", havingValue = "true")
public class KeycloakAdminRestTemplate extends OAuth2RestTemplate {
	
	public KeycloakAdminRestTemplate(OAuth2ProtectedResourceDetails resource) {
		super(resource);
	}
	
	public KeycloakAdminRestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {		
		super(resource, context);
	}

}
