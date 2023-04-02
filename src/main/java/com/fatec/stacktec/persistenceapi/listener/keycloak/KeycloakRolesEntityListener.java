package com.fatec.stacktec.persistenceapi.listener.keycloak;

import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.keycloak.converter.KeycloakRolesConverter;
import com.fatec.stacktec.persistenceapi.model.user.Permission;
import com.fatec.stacktec.persistenceapi.service.keycloak.KeycloakRolesService;
import com.fatec.stacktec.searchapi.util.BeanUtil;

@Service
public class KeycloakRolesEntityListener {
	
	private static KeycloakRolesConverter converter;
	
	@Autowired
	public void setKeycloakPermissionConverter(KeycloakRolesConverter converter) {
		KeycloakRolesEntityListener.converter = converter;
	}
	
	@PrePersist
	public void methodExecuteAfterSave(Permission permission) {
		KeycloakRolesService service = BeanUtil.getBean(KeycloakRolesService.class);
		KeycloakRoles keycloakRoles = converter.convert(permission);
		KeycloakRoles keycloakPermission = service.createRoles(keycloakRoles);
		if(keycloakPermission != null) {
			permission.setKeycloakId(keycloakPermission.getId());
		}
	}

}
