package com.fatec.stacktec.persistenceapi.enumeration;

import lombok.Getter;

@Getter
public enum GenderType {
	
	MALE("M", "Masculino"),
	FEMALE("F", "FEMININO"),
	UNDEFINED("U", "Indefinido"),
	OTHER("O", "Outro");
	
	private final String label;
	private final String chave;
	
	GenderType(String chave, String label){
		this.label = label;
		this.chave = chave;
	}
	
	public static GenderType valueOf(Integer ordinal) {
		if(ordinal != null) {
			for(GenderType genderType : GenderType.values()) {
				if(genderType.ordinal() == ordinal) {
					return genderType;
				}
			}
		}
		return null;
	}
}
