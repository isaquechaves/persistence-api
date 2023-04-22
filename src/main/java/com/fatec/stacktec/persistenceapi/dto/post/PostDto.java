package com.fatec.stacktec.persistenceapi.dto.post;

import java.util.Date;
import java.util.List;

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
	private String titulo;
	
	@JsonProperty 
	private String descricao;
	
	@JsonProperty 
	private Long autor;
	
	@JsonProperty 
	private List<PostImageDto> images;
	
	@JsonProperty 
	private List<PostComentarioDto> comentarios;
	
	@JsonProperty 
	private List<PostTagDto> tags;
	
	@JsonProperty 
	private List<RespostaDto> respostas;	
	
	@JsonProperty 
	private Long disciplina;
	
	@JsonProperty 
	private Date criadoEm;
	
	@JsonProperty 
	private Date atualizadoEm;
	
}
