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
@ApiModel(value = "RespostaDetail", description = "Sample model for post detail")
public class RespostaDto {
	
	
	private String descricao;
	
	
	private Long autor;
	
	
	private Integer votos;
	
	
	private Boolean aceita;
	
	private Long post;
	
	@JsonProperty 
	private List<RespostaImageDto> images;
	
	@JsonProperty
	private List<RespostaComentarioDto> comentarios;
	
	private Date criadoEm;
	
	@JsonProperty 
	private Date atualizadoEm;
}
