package com.fatec.stacktec.persistenceapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("persistence")
@Data
public class PersistenceApiProperty {
	
	@Value("{keycloak.enabled:false}")
	private boolean keycloakEnabled;
	
	private OAuthCredentials oAuthCredentials;
	
	private Integer defaultPageSize = 2500;
	
	@Data
	public static class OAuthCredentials {
		private Boolean enabled;
		private String username;
		private String clientId;
		private String clientSecret;
		private String grantType;
		private String password;
		private String authorizationServerUrl;
		private String resourceServerUrl;
		private String resourceServerUrlRole;
	}
}
