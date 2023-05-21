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
@ApiModel(value = "RespostaDetail", description = "Sample model for post detail")
public class RespostaDto {
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private String descricao;
	
	@JsonProperty
	private Long postId;
	
	@JsonProperty
	private Long autorId;
	
	@JsonProperty
	private Integer votos;
	
	@JsonProperty
	private Boolean aceita;

	@JsonIgnore
	private Set<RespostaComentarioDto> comentarios;
	
	@JsonIgnore
	private Date criadoEm;
	
	@JsonIgnore
	private Date atualizadoEm;
}
