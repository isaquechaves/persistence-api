package com.fatec.stacktec.persistenceapi.model.util;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NoGeneratorIdentifierEntity<ID extends Serializable> extends BaseModel<ID> {
	
	@Id
	private ID id;
	
	@Override
	public ID getId() {
		return id;
	}

	@Override
	public void setId(ID id) {
		this.id = id;
	}

}
