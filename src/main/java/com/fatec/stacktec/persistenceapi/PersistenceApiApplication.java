package com.fatec.stacktec.persistenceapi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;


import lombok.extern.java.Log;

@Log
@EnableCaching
@EnableConfigurationProperties
@ComponentScan(basePackages= {"com.fatec.stacktec"})
@ContextConfiguration(classes = {PersistenceApiApplication.class})
@SpringBootApplication
public class PersistenceApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PersistenceApiApplication.class, args);
	}
	
}
