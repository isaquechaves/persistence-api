package com.fatec.stacktec.persistenceapi.model.keycloak;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class KeycloakUser {
	
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String username;
	private boolean enable = true;
	private boolean emailVeried = false;
	private Map<String,Object> attributes;
	private List<String> realmRoles = new ArrayList<>();	
	private Long createdTimestamp;
}
