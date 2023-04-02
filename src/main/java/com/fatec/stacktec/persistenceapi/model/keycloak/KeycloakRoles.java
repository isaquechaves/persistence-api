package com.fatec.stacktec.persistenceapi.model.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakRoles {
	
	private String id;
	private String name;
	private String description;
}
