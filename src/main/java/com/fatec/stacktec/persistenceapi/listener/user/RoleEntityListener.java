package com.fatec.stacktec.persistenceapi.listener.user;

import java.util.Optional;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import com.fatec.stacktec.persistenceapi.model.user.Role;
import com.fatec.stacktec.persistenceapi.search.facade.user.RoleSearchServiceFacade;
import com.fatec.stacktec.searchapi.holder.user.RoleHolder;
import com.fatec.stacktec.searchapi.search.ESUtil;
import com.fatec.stacktec.searchapi.util.BeanUtil;

public class RoleEntityListener {
	
	@PostUpdate
	@PostPersist
	public void methodExecuteAfterSave(final Role reference) {
		RoleSearchServiceFacade service = BeanUtil.getBean(RoleSearchServiceFacade.class);
		Optional<RoleHolder> roleHolderOptional = service.fillHolderElement(reference);
		roleHolderOptional.ifPresent(service::save);
	}
	
	@PostRemove
	public void methodExecuteAfterRemove(final Role reference) {
		ESUtil.createIndex(RoleHolder.class);
		RoleSearchServiceFacade service = BeanUtil.getBean(RoleSearchServiceFacade.class);
		service.delete(reference.getId());
	}
}
