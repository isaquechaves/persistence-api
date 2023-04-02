package com.fatec.stacktec.persistenceapi.exception;

import java.util.HashMap;
import java.util.Map;

import com.fatec.stacktec.persistenceapi.enumeration.BusinessExceptionCode;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fatec.stacktec.persistenceapi.enumeration.BusinessExceptionCode.UNKNOWN;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
	
	private Map<BusinessExceptionCode, String> errorsMap;
	
	public BusinessException(String message) {
		super(message);
		if(errorsMap == null)
			errorsMap = new HashMap<>();
		errorsMap.put(UNKNOWN, message);
	}
	
	public BusinessException(BusinessExceptionCode code, String message) {
		super(message);
		if(errorsMap == null)
			errorsMap = new HashMap<>();
		errorsMap.put(code, message);
	}
	
	public BusinessException(Map<BusinessExceptionCode, String> errorsMap, String message) {
		super(message);
		this.errorsMap = errorsMap;
	}
}
