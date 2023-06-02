package com.fatec.stacktec.persistenceapi.dto.post;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMinimalDto implements Serializable{
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private String titulo;
	
	@JsonProperty
	private Date criadoEm;

}
