package com.fatec.stacktec.persistenceapi.enumeration;

import lombok.Getter;

@Getter
public enum BusinessExceptionCode {
	
	UNKNOWN(1),
	
	EMAIL_INVALID(9000),
	USER_LOGGED_DIFFERENT_EMAIL(9001),
	USER_VALIDATION_ERROR(9002);
	
	private Integer code;
	
	BusinessExceptionCode(Integer code){
		this.code = code;
	}
	
}
