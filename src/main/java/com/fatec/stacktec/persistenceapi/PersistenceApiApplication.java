package com.fatec.stacktec.persistenceapi;


import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public static ModelMapper ModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}
	
   @PostConstruct
    public void init(){
      // Setting Spring Boot SetTimeZone
      TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
