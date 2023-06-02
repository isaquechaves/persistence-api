package com.fatec.stacktec.persistenceapi.dto.post;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPageDto implements Serializable {
	
	@JsonProperty
	Integer totalPages;	
	
	@JsonProperty
	Integer maxResults;
	
	@JsonProperty
	List<PostMinimalDto> postDtos;
	
}
