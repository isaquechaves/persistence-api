package com.fatec.stacktec.persistenceapi.dto.tag;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatec.stacktec.persistenceapi.dto.post.TagDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagPageDto implements Serializable {
	
	@JsonProperty
	private Integer totalPages;
	
	@JsonProperty
	private Integer maxResults;
	
	@JsonProperty
	private List<TagDto> tagDtos;
}
