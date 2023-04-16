package com.fatec.stacktec.persistenceapi.payload.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse implements Serializable {
	
	private String message;
}
