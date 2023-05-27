package com.fatec.stacktec.persistenceapi.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "role")
@EqualsAndHashCode(callSuper = true, exclude = "userInternal")
@ToString(exclude = "userInternal")
public class Role extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
	
	@Column 
	private String name;
	
	@Column
	private String description;
	
	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
			mappedBy = "roles")
	private Set<UserInternal> userInternal = new HashSet<>(0);
	
}
