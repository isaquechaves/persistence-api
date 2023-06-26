package com.fatec.stacktec.persistenceapi.service.user;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.user.Role;
import com.fatec.stacktec.persistenceapi.repository.user.RoleRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

@Service
public class RoleService extends CrudServiceJpaImpl<RoleRepository, Role>{
		
	public Role findByName(String name) {
		return repository.findByName(name);
	}
}
