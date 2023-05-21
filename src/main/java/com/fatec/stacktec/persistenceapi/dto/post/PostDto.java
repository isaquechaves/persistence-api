package com.fatec.stacktec.persistenceapi.dto.post;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PostDetail", description = "Sample model for post detail")
public class PostDto {
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private Long autorId;	
	
	@JsonProperty 
	private String titulo;
	
	@JsonProperty 
	private String descricao;	
	
	@JsonIgnore
	private Set<PostComentarioDto> comentarios;
	
	@JsonProperty 
	private Set<String> tags;
	
	@JsonIgnore
	private Set<RespostaDto> respostas;	
	
	@JsonProperty 
	private Long disciplinaId;
	
	@JsonProperty 
	private Date criadoEm;
	
	@JsonProperty 
	private Date atualizadoEm;
	
}
