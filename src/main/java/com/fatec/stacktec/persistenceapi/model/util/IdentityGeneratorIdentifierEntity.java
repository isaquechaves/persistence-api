package com.fatec.stacktec.persistenceapi.model.util;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class IdentityGeneratorIdentifierEntity<ID extends Serializable> extends BaseModel<ID> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private ID id;
	
	public ID getId() {
		return id;
	}
	
	public void setId(ID id) {
		this.id = id;
	}

}
