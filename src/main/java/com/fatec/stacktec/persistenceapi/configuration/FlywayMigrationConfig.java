package com.fatec.stacktec.persistenceapi.configuration;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import com.fatec.stacktec.searchapi.search.ESBaseStoreServiceImpl;

import lombok.Setter;
import lombok.extern.java.Log;

@Log
@Setter
@Configuration
@ConfigurationProperties(prefix = "stacktec")
public class FlywayMigrationConfig {
	
	private final ApplicationContext applicationContext;
	
	private final DataSource dataSource;
	
	private boolean flywayEnabled;
	
	public FlywayMigrationConfig(ApplicationContext applicationContext, DataSource dataSource) {
		this.applicationContext = applicationContext;
		this.dataSource = dataSource;
	}
	
	@Bean
	public void flywayMigrate() {
		if(flywayEnabled) {
			log.info("Flyway migration enabled!");
			int migrated = Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
			
			if(migrated > 0) {
				ConfigurableListableBeanFactory listableBeanFactory = ((AbstractApplicationContext) applicationContext).getBeanFactory();
				for(String name : applicationContext.getBeanDefinitionNames()) {
					Object s = listableBeanFactory.getSingleton(name);
					if(s instanceof ESBaseStoreServiceImpl) {
						((ESBaseStoreServiceImpl) s).deleteAll();
						((ESBaseStoreServiceImpl) s).getInitialElements();
					}
				}
			}
		}
	}
}
