package com.fatec.stacktec.persistenceapi.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ComentarioDetail", description = "Sample model for comentario detail")
public class ComentarioDto {

	@JsonProperty
	private Long id;
	
	@JsonProperty
	private Long postId;

	@JsonProperty
	private Long respostaId;
	
	@JsonProperty
	private Long autor;
	
	@JsonProperty
	private String texto;
		 
}
