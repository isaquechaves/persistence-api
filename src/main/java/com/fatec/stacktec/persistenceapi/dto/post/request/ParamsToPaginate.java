package com.fatec.stacktec.persistenceapi.dto.post.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamsToPaginate {
	
	@JsonProperty
	private List<String> ordens;
	
	@JsonProperty
	private List<String> tags;
}
