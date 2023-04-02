package com.fatec.stacktec.persistenceapi.keycloak.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import com.fatec.stacktec.persistenceapi.model.keycloak.KeycloakRoles;
import com.fatec.stacktec.persistenceapi.model.user.Permission;

@Component
public class KeycloakRolesConverter {
	
	private final ModelMapper modelMapper;
	
	private TypeMap<Permission, KeycloakRoles> typeMapPermission;
}
