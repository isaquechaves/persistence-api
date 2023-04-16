package com.fatec.stacktec.persistenceapi.dto;

import java.util.Set;

import lombok.Data;

@Data
public class SignUpDto {		
    private String name;
    private String apelido;
    private String email;
    private String password;
    Set<Long> roles;
}
