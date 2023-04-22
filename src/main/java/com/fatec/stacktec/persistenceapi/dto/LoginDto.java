package com.fatec.stacktec.persistenceapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "LoginDetail", description = "Sample model for login detail")
public class LoginDto {
	
	@JsonProperty
	private String email;
	
	@JsonProperty
	private String password;
}
