package com.fatec.stacktec.persistenceapi.util;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImgData implements Serializable {
	
	@JsonProperty
	private Long imageId;	
}
