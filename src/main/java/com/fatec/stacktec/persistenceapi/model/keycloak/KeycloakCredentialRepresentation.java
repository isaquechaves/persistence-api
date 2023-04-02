package com.fatec.stacktec.persistenceapi.model.keycloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KeycloakCredentialRepresentation {
	
	private String type;
	private boolean temporary = false;
	private String value;

}
