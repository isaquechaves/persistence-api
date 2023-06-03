package com.fatec.stacktec.persistenceapi.payload.response;

import java.io.Serializable;
import java.util.List;


import lombok.Data;

@Data
public class UserInfoResponse implements Serializable {
	
	private String nome;
	private String email;
	private List<String> roles;
	private Long id;
	private String cookie;
	
	public UserInfoResponse(String nome, String email, List<String>  roles, Long id, String cookie) {
		this.nome = nome;
		this.email = email;
		this.roles = roles;
		this.id = id;
		this.cookie = cookie;
	}
}
