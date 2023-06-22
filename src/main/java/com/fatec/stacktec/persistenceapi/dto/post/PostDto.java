package com.fatec.stacktec.persistenceapi.dto.post;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatec.stacktec.persistenceapi.enumeration.PostStatus;

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
	
	@JsonProperty
	private List<PostComentarioDto> comentarios;
	
	@JsonProperty 
	private Set<String> tags;
	
	@JsonProperty
	private List<RespostaDto> respostas;	
	
	@JsonProperty 
	private Long disciplinaId;
	
	@JsonProperty 
	private Date criadoEm;
	
	@JsonProperty 
	private Date atualizadoEm;
	
	@JsonProperty
	private PostStatus postStatus;
	
	@JsonProperty
	private Integer votos;
	
	@JsonProperty
	private boolean votado;
}
