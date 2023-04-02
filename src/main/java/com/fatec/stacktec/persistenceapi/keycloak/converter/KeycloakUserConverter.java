package com.fatec.stacktec.persistenceapi.keycloak.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fatec.stacktec.persistenceapi.model.keycloak.KeycloakUser;
import com.fatec.stacktec.persistenceapi.model.user.UserSite;
import com.fatec.stacktec.persistenceapi.service.keycloak.KeycloakUserService;

@Component
public class KeycloakUserConverter {
	
	public static final String APPS = "apps";
	public static final String USERSITE_ID = "usersite_id";
	
	private final KeycloakUserService keycloakUserService;
	
	public KeycloakUserConverter(KeycloakUserService keycloakUserService) {
		this.keycloakUserService = keycloakUserService;
	}
	
	public KeycloakUser convert(UserSite userSite) {
		KeycloakUser keycloakUser = new KeycloakUser();
		if(userSite.getKeycloakId() != null) {
			keycloakUser = keycloakUserService.getUserById(userSite.getKeycloakId());
		}
		keycloakUser.setEnable(true);
		keycloakUser.setId(userSite.getKeycloakId());
		keycloakUser.setFirstName(userSite.getName());
		keycloakUser.setUsername(userSite.getEmail());
		keycloakUser.setEmail(userSite.getEmail().trim().toLowerCase());
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(USERSITE_ID, userSite.getId());
		keycloakUser.setAttributes(attributes);
		return keycloakUser;
	}
}
