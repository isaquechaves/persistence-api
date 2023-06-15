package com.fatec.stacktec.persistenceapi.enumeration;

import lombok.Getter;

@Getter
public enum PostStatus {
	
	ABERTO("Aberto"),
	FECHADO("Fechado"),
	RESPONDIDO("Respondido");
	
	private final String status;
	
	PostStatus(String status) {
		this.status = status;
	}
}
