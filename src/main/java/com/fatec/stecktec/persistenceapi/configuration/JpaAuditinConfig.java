package com.fatec.stecktec.persistenceapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fatec.stecktec.persistenceapi.auditor.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditinConfig {
	
	@Bean
	public AuditorAware<String> auditorAware(){
		return new AuditorAwareImpl();
	}

}
