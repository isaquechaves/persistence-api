package com.fatec.stacktec.persistenceapi.search.facade.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;
import com.fatec.stacktec.searchapi.holder.UserInternalHolder;
import com.fatec.stacktec.searchapi.repository.user.UserInternalSearchRepository;
import com.fatec.stacktec.searchapi.search.ESBaseStoreServiceImpl;

import lombok.extern.java.Log;

@Log
@Service
public class UserInternalSearchServiceFacade extends ESBaseStoreServiceImpl<UserInternalSearchRepository, UserInternalHolder, UserInternal> {
	
	private final UserInternalService userInternalService;
	
	public UserInternalSearchServiceFacade(UserInternalService userInternalService) {
		this.userInternalService = userInternalService;
	}
	
	@PostConstruct
	public void getInitialElements() {
		config(UserInternalHolder.class);
		//load data
		long count = repository.count();
		log.info("Checking cache UserInternalHolder - elements: " + count);
		if(elasticsearchConfig.isLoadSearchDataOnInit() || count == 0) {
			List<UserInternalHolder> userInternalHolders = new ArrayList<>();
			List<UserInternal> userInternalList = userInternalService.findAll();
			for(UserInternal userInternal : userInternalList) {
				Optional<UserInternalHolder> holderOptional = fillHolderElement(userInternal);
				holderOptional.ifPresent(userInternalHolders::add);
			}
			if(!userInternalHolders.isEmpty())
				repository.saveAll(userInternalHolders);			
		}
	}
	

	@Override
	public Optional<UserInternalHolder> fillHolderElement(UserInternal model) {
		return model.populateForCache();
	}

}
