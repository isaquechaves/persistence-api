package com.fatec.stacktec.persistenceapi.dto.post;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PostTagDetail", description = "Sample model for tag detail")
public class PostTagDto {
	
	@JsonProperty
	private Long id;
	
	@JsonIgnore
	private Long postId;
	
	@JsonProperty
	private String nome;
	
	@JsonProperty
	private Integer qtdePosts;
}
