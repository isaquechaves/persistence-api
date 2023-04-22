package com.fatec.stacktec.persistenceapi.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatec.stacktec.persistenceapi.util.ImgData;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PostImageDetail", description = "Sample model for image post detail")
public class PostImageDto extends ImgData {
	
	@JsonIgnore
	private Long postId;
}
