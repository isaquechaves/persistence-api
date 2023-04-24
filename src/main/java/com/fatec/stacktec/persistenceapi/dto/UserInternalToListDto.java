package com.fatec.stacktec.persistenceapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatec.stacktec.searchapi.enumeration.SemestreType;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(value = "SignUpDetail", description = "Sample model for SignUp detail")
public class UserInternalToListDto {
		
		@JsonProperty
	    private String name;
		
		@JsonProperty
	    private String apelido;
		
		@JsonProperty
	    private String email;
		
		@JsonProperty
		private SemestreType semestre;
	
}
