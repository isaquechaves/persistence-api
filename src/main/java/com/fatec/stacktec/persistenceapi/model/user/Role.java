package com.fatec.stacktec.persistenceapi.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fatec.stacktec.persistenceapi.listener.user.RoleEntityListener;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;
import com.fatec.stacktec.searchapi.holder.user.RoleHolder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "role")
@EqualsAndHashCode(callSuper = true, exclude = "userInternal")
@EntityListeners({RoleEntityListener.class})
public class Role extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
	
	@Column 
	private String name;
	
	@Column
	private String description;
	
	@ManyToMany(
			fetch = FetchType.LAZY,
			targetEntity = UserInternal.class,
			cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE},
			mappedBy = "roles")
	private Set<UserInternal> userInternal = new HashSet<>(0);

	
	@Override
	public Optional<RoleHolder> populateForCache() {
		return Optional.of(new RoleHolder(getId(), getName()));
	}
}
