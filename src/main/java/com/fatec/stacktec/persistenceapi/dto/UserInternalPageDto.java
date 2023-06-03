package com.fatec.stacktec.persistenceapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInternalPageDto {
	
	@JsonProperty
	Integer totalPages;	
	
	@JsonProperty
	Integer maxResults;
	
	@JsonProperty
	List<UserInternalMinimalToPageDto> users;
}
