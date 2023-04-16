package com.fatec.stacktec.persistenceapi.payload.response;

import java.io.Serializable;
import java.util.List;


import lombok.Data;

@Data
public class UserInfoResponse implements Serializable {
	
	private String email;
	private List<String> roles;
	
	public UserInfoResponse(String email, List<String>  roles) {
		this.email = email;
		this.roles = roles;
	}
}
