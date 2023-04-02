package com.fatec.stacktec.persistenceapi.service.keycloak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import com.fatec.stacktec.persistenceapi.configuration.PersistenceApiProperty;
import com.fatec.stacktec.persistenceapi.exception.BusinessException;
import com.fatec.stacktec.persistenceapi.keycloak.util.KeycloakUtil;
import com.fatec.stacktec.persistenceapi.model.keycloak.KeycloakCredentialRepresentation;
import com.fatec.stacktec.persistenceapi.model.keycloak.KeycloakRoles;
import com.fatec.stacktec.persistenceapi.model.keycloak.KeycloakUser;
import com.fatec.stacktec.persistenceapi.util.KeycloakAdminRestTemplate;

import lombok.extern.java.Log;

@Log
@Service
public class KeycloakUserService {
	
	private final PersistenceApiProperty persistenceProperty;
	
	private final KeycloakAdminRestTemplate keycloakAdminRestTemplate;
	
	public KeycloakUserService(PersistenceApiProperty persistenceProperty, @Autowired(required = false) KeycloakAdminRestTemplate keycloakAdminRestTemplate) {
		this.persistenceProperty = persistenceProperty;
		this.keycloakAdminRestTemplate = keycloakAdminRestTemplate;
	}
	
	public String create(KeycloakUser keycloakUser) {
		if(keycloakUser.getEmail() == null) {
			throw new BusinessException("Email is required");
		}
		KeycloakUser dto = getUserByEmail(keycloakUser.getEmail());
		if(dto == null) {
			try {
				HttpEntity<Object> entity = KeycloakUtil.buildHttpEntity(keycloakUser);
				ResponseEntity<KeycloakUser> postReturn = keycloakAdminRestTemplate.postForEntity(persistenceProperty.getOAuthCredentials().getResourceServerUrl(), entity, KeycloakUser.class);
				if(postReturn.getStatusCode() == HttpStatus.CREATED) {
					ResponseEntity<KeycloakUser> keycloakUserReturn = keycloakAdminRestTemplate.getForEntity(postReturn.getHeaders().getLocation(), KeycloakUser.class);
					if(keycloakUserReturn.getStatusCode() == HttpStatus.OK) {
						return keycloakUserReturn.getBody().getId();
					}
				}
			} catch (HttpClientErrorException e) {
				if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					keycloakAdminRestTemplate.getAccessToken();
					log.info("Get a new token");
					throw new BusinessException("Keycloak session not found");
				}
			}
		}else {
			updateUser(keycloakUser);
		}
		return keycloakUser.getId();
	}
	
	public KeycloakUser getUserById(String keycloakId) {
		if(keycloakId == null)
			throw new BusinessException("KeycloakId is missing");
		refreshToken();
		KeycloakUser user = null;
		try {
			user = keycloakAdminRestTemplate.getForObject(persistenceProperty.getOAuthCredentials().getResourceServerUrl() + "/" + keycloakId, KeycloakUser.class);
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("Get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}
		return user;
	}
	
	private void updateUser(KeycloakUser keycloakUserDto) {
		if(keycloakUserDto.getId() == null)
			throw new BusinessException("Keycloak User ID is missing");
		refreshToken();
		HttpEntity<Object> httpEntity = KeycloakUtil.buildHttpEntity(keycloakUserDto);
		try {
			keycloakAdminRestTemplate.put(persistenceProperty.getOAuthCredentials() + "/" + keycloakUserDto.getId(), httpEntity);			
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}
		
	}
	
	private void disableUser(String keycloakUserId) {
		if(keycloakUserId == null)
			throw new BusinessException("Keycloak User ID is missing");
		refreshToken();
		
		Map<String, Boolean> attr = new HashMap<>();
		attr.put("enabled", false);
		HttpEntity<Object> httpEntity = KeycloakUtil.buildHttpEntity(attr);
		try {
			keycloakAdminRestTemplate.put(persistenceProperty.getOAuthCredentials() + "/" + keycloakUserId, httpEntity);			
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}		
	}
	
	private void removeUser(String keycloakUserId) {
		if(keycloakUserId == null)
			throw new BusinessException("Keycloak User ID is missing");
		refreshToken();			
		try {
			keycloakAdminRestTemplate.delete(persistenceProperty.getOAuthCredentials() + "/" + keycloakUserId);			
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}		
	}
	
	private void addRoles(List<KeycloakRoles> roles, String keycloakUserId) {
		if(keycloakUserId == null)
			throw new BusinessException("Keycloak User ID is missing");
		if(roles != null && !roles.isEmpty()) {
			refreshToken();			
			try {
				String url = persistenceProperty.getOAuthCredentials().getResourceServerUrl() + "/" + keycloakUserId + "/roles-mappings/realm";
				log.info("KeycloakUserService.addRoles - " + url + " " + KeycloakUtil.buildHttpEntity(roles));
				keycloakAdminRestTemplate.postForEntity(url, KeycloakUtil.buildHttpEntity(roles), Object.class);			
			} catch (HttpClientErrorException e) {
				if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					keycloakAdminRestTemplate.getAccessToken();
					log.info("get a new token");
					throw new BusinessException("Keycloak session not found");
				}
			}		
		}
	}
	
	private void removeRoles(List<KeycloakRoles> roles, String keycloakUserId) {
		if(keycloakUserId == null)
			throw new BusinessException("Keycloak User ID is missing");
		if(roles != null && !roles.isEmpty()) {
			refreshToken();					
			try {
				String url = persistenceProperty.getOAuthCredentials().getResourceServerUrl() + "/" + keycloakUserId + "/roles-mappings/realm";
				MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
				header.add("Content-Type", "application/json");
				HttpEntity<Object> httpEntity = new HttpEntity<Object>(roles, header);
				keycloakAdminRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity,  KeycloakRoles.class);			
			} catch (HttpClientErrorException e) {
				if(e.getStatusCode() != HttpStatus.NOT_FOUND) {
					throw new BusinessException("Error: " + e.getMessage());
				}
				if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					keycloakAdminRestTemplate.getAccessToken();
					log.info("get a new token");
					throw new BusinessException("Keycloak session not found");
				}
			}		
		}
	}
	
	private KeycloakUser getUserByEmail(String email) {
		if(email == null)
			throw new BusinessException("Email is missing");
		refreshToken();
		try {
			KeycloakUser[] user = keycloakAdminRestTemplate.getForObject(persistenceProperty.getOAuthCredentials().getResourceServerUrl() +"?email=" + email.toLowerCase(), KeycloakUser[].class);
			if(user != null && user.length > 0) {
				for(KeycloakUser element : user) {
					if(element.getEmail() != null && element.getEmail().toLowerCase().equals(email.toLowerCase())) {
						element.setUsername(email.toLowerCase());
						return element;
					}
				}
			}
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("Get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}
		return null;
	}
	
	public void resetPassword(String keycloakId, String newPassword, boolean temporary) {
		if(keycloakId == null)
			throw new BusinessException("KeycloakId is missing");
		refreshToken();
		try {
			String url = persistenceProperty.getOAuthCredentials().getResourceServerUrl() + "/" + keycloakId + "/reset-password";
			KeycloakCredentialRepresentation keycloakCredentialRepresentation = new KeycloakCredentialRepresentation();
			keycloakCredentialRepresentation.setTemporary(temporary);
			keycloakCredentialRepresentation.setType("password");
			keycloakCredentialRepresentation.setValue(newPassword);
			keycloakAdminRestTemplate.put(url, KeycloakUtil.buildHttpEntity(keycloakCredentialRepresentation), Object.class);
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("Get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}
	}
	
	public void sendVerificationEmail(String keycloakId) {
		if(keycloakId == null)
			throw new BusinessException("KeycloakId is missing");
		refreshToken();
		try {
			String url = persistenceProperty.getOAuthCredentials().getResourceServerUrl() + "/" + keycloakId + "/send-verify-email";
			KeycloakCredentialRepresentation keycloakCredentialRepresentation = new KeycloakCredentialRepresentation();		
			keycloakAdminRestTemplate.put(url, KeycloakUtil.buildHttpEntity(keycloakCredentialRepresentation), Object.class);
			log.info("sent verification email " + keycloakId);
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("Get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}
	}	

	public void sendEmailToRecoverPassword(String keycloakId) {
		if(keycloakId == null)
			throw new BusinessException("KeycloakId is missing");
		refreshToken();
		try {
			String url = persistenceProperty.getOAuthCredentials().getResourceServerUrl() + "/" + keycloakId + "/execute-actions-email";
			String body = "[\"UPDATE_PASSWORD\"]";
			keycloakAdminRestTemplate.put(url, KeycloakUtil.buildHttpEntity(body), Object.class);
			log.info("sending email to recover password " + keycloakId);
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				keycloakAdminRestTemplate.getAccessToken();
				log.info("Get a new token");
				throw new BusinessException("Keycloak session not found");
			}
		}
	}
	
	private void refreshToken() {
		try {
			keycloakAdminRestTemplate.getAccessToken();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
