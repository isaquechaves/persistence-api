package com.fatec.stacktec.persistenceapi.listener.user;

import java.util.Optional;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.search.facade.user.UserInternalSearchServiceFacade;
import com.fatec.stacktec.searchapi.holder.UserInternalHolder;
import com.fatec.stacktec.searchapi.search.ESUtil;
import com.fatec.stacktec.searchapi.util.BeanUtil;

public class UserInternalEntityListener {
	
	@PostUpdate
	@PostPersist
	public void methodExecuteAfterSave(final UserInternal reference) {
		UserInternalSearchServiceFacade service = BeanUtil.getBean(UserInternalSearchServiceFacade.class);
		Optional<UserInternalHolder> userInternalHolderOptional = service.fillHolderElement(reference);
		userInternalHolderOptional.ifPresent(service::save);
	}
	
	@PostRemove
	public void methodExecuteAfterRemove(final UserInternal reference) {
		ESUtil.createIndex(UserInternalHolder.class);
		UserInternalSearchServiceFacade service = BeanUtil.getBean(UserInternalSearchServiceFacade.class);
		service.delete(reference.getId());		
	}
}
