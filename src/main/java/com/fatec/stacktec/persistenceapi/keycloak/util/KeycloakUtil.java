package com.fatec.stacktec.persistenceapi.keycloak.util;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class KeycloakUtil {
	
	public static HttpEntity<Object> buildHttpEntity(Object object) {
		HttpEntity<Object> entity = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(!ObjectUtils.isEmpty(object)) {
			Gson gson = new Gson();
			String jsonStr = gson.toJson(object);
			if(object instanceof ArrayList || object instanceof Object[])
				entity = new HttpEntity<>(gson.fromJson(jsonStr, JsonArray.class), headers);
			else
				entity = new HttpEntity<>(gson.fromJson(jsonStr, JsonObject.class), headers);
		}else {
			entity = new HttpEntity<>(null, headers);
		}
		return entity;		
	}
	
	public static Long getUserIdByPrincipal(Principal principal) {
		if(principal == null) return null;
		if(principal instanceof KeycloakAuthenticationToken) {
			if(((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken() != null) {				
				Map<String, Object> attributes = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken().getOtherClaims(); 
				if(attributes.containsKey(KeycloakAuthenticationToken.USERSITE_ID)) {
					String userId = String.valueOf(attributes.get(KeycloakUserConverter.USERSITE_ID));
					if(userId.contains("."))
						userId = userId.split("\\.")[0];
					return userId != null && !userId.isEmpty() ? Long.parseLong((userId) : null;
				}
			}
		}
	}
}
