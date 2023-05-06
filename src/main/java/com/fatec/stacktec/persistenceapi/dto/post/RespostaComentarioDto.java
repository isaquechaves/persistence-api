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
@ApiModel(value = "RespostaComentarioDetail", description = "Sample model for comentario resposta detail")
public class RespostaComentarioDto {

		@JsonProperty
		private Long id;
		
		@JsonIgnore
		private Long respostaId;
		
		@JsonProperty
		private Long autor;
		
		@JsonProperty
		private String texto;
			 
	}
