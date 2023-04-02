package com.fatec.stecktec.persistenceapi.util;

import java.security.Principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuditorUtil {
	
	public static String getEmailByPrincipal(Principal principal) {
		if(principal==null) return null;
				
		if(principal instanceof AnonymousAuthenticationToken) {
			return principal.getName();
		}
		
		return null;
	}
}
