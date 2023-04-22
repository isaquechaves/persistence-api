package com.fatec.stacktec.persistenceapi.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "SignUpDetail", description = "Sample model for SignUp detail")
public class SignUpDto {	
	
	@JsonProperty
    private String name;
	
	@JsonProperty
    private String apelido;
	
	@JsonProperty
    private String email;
	
	@JsonProperty
    private String password;
	
	@JsonProperty
    Set<Long> roles;
}
