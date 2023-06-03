package com.fatec.stacktec.persistenceapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatec.stacktec.persistenceapi.dto.post.PostMinimalDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostPageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInternalMinimalToPageDto {
	
	@JsonProperty
    private Long id;
	
	@JsonProperty
    private String apelido;
	
	@JsonProperty
    private String email;
	
	@JsonProperty
	private List<String> roles;
}
