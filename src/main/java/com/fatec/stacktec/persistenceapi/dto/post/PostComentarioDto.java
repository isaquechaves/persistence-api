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
@ApiModel(value = "PostComentarioDetail", description = "Sample model for comentario post detail")
public class PostComentarioDto {
	
	@JsonProperty
	private Long id;
	
	@JsonIgnore
	private Long postId;
	
	@JsonProperty
	private Long autorId;
	
	@JsonProperty
	private String autorApelido;
	
	@JsonProperty
	private String texto;
		 
}
