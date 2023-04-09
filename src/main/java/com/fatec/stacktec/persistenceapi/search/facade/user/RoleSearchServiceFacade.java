package com.fatec.stacktec.persistenceapi.search.facade.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.user.Role;
import com.fatec.stacktec.persistenceapi.service.user.RoleService;
import com.fatec.stacktec.searchapi.holder.user.RoleHolder;
import com.fatec.stacktec.searchapi.repository.user.RoleSearchRepository;
import com.fatec.stacktec.searchapi.search.ESBaseStoreServiceImpl;

import lombok.extern.java.Log;

@Log
@Service
public class RoleSearchServiceFacade extends ESBaseStoreServiceImpl<RoleSearchRepository, RoleHolder, Role>{
	
	private final RoleService roleService;

	public RoleSearchServiceFacade(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@PostConstruct
	public void getInitialElements() {
		config(RoleHolder.class);
		long count = repository.count();
		log.info("Checking cache RoleHolder - elements: " + count);
		if(elasticsearchConfig.isLoadSearchDataOnInit() || count == 0) {
			List<RoleHolder> roleHolders = new ArrayList<>();
			List<Role> roleList = roleService.findAll();
			for(Role role : roleList) {
				Optional<RoleHolder> roleOptional = role.populateForCache();
				roleOptional.ifPresent(roleHolders::add);
			}
			if(!roleHolders.isEmpty())
				repository.saveAll(roleHolders);
		}
	}

	@Override
	public Optional<RoleHolder> fillHolderElement(Role model) {
		return model.populateForCache();
		
	}

}
