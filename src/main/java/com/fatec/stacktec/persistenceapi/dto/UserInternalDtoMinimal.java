package com.fatec.stacktec.persistenceapi.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatec.stacktec.persistenceapi.enumeration.SemestreType;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "SignUpDetail", description = "Sample model for SignUp detail")
public class UserInternalDtoMinimal {	
	
	@JsonProperty
    private String name;
	
	@JsonProperty
    private String apelido;
	
	@JsonProperty
    private String email;
	
	@JsonProperty
    private String password;
	
	@JsonProperty
	private SemestreType semestre;
	
}
