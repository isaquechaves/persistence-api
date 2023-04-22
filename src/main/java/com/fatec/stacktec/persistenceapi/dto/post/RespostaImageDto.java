package com.fatec.stacktec.persistenceapi.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(value = "PressKitDetail", description = "Sample model for image detail")
public class RespostaImageDto extends ImgData {
	
	@JsonIgnore
	private Long respostaId;
}
