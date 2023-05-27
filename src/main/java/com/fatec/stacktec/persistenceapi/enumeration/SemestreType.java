package com.fatec.stacktec.persistenceapi.enumeration;

import lombok.Getter;

@Getter
public enum SemestreType {
	
	SEM_1(1, "1º Semestre"),
	SEM_2(2, "2º Semestre"),
	SEM_3(3, "3º Semestre"),
	SEM_4(4, "4º Semestre"),
	SEM_5(5, "5º Semestre"),
	SEM_6(6, "6º Semestre"),
	SEM_7(7, "7º Semestre"),
	SEM_8(8, "8º Semestre"),
	SEM_9(9, "9º Semestre"),
	SEM_10(10, "10º Semestre"),
	SEM_11(11, "11º Semestre"),
	SEM_12(12, "12º Semestre");
	
	private final Integer numero;
	private final String nome;
	
	SemestreType(Integer numero, String nome) {
		this.numero = numero;
		this.nome = nome;
	}
	
}