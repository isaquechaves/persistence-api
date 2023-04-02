package com.fatec.stacktec.persistenceapi.auditor;

import java.security.Principal;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fatec.stacktec.persistenceapi.util.AuditorUtil;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		String userId = AuditorUtil.getEmailByPrincipal(principal);
		return principal != null && userId != null ? Optional.of(String.valueOf(userId)) : Optional.empty();		
	}

}
