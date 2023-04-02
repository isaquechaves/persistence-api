package com.fatec.stacktec.persistenceapi.service.keycloak;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.configuration.PersistenceApiProperty;
import com.fatec.stacktec.persistenceapi.keycloak.util.KeycloakUtil;
import com.fatec.stacktec.persistenceapi.model.keycloak.KeycloakRoles;
import com.fatec.stacktec.persistenceapi.util.KeycloakAdminRestTemplate;

@Service
public class KeycloakRolesService {
	
	private final PersistenceApiProperty persistenceProperty;
	
	private final KeycloakAdminRestTemplate  keycloakAdminRestTemplate;
	
	public KeycloakRolesService(PersistenceApiProperty persistenceProperty, @Autowired(required = false) KeycloakAdminRestTemplate keycloakAdminRestTemplate) {
		this.persistenceProperty = persistenceProperty;
		this.keycloakAdminRestTemplate = keycloakAdminRestTemplate;
	}
	
	public KeycloakRoles createRoles(KeycloakRoles keycloakPermission) {
		HttpEntity<Object> httpEntity = KeycloakUtil.buildHttpEntity(keycloakPermission);
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
