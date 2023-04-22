package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.Data;

@Data
@Entity
public class Disciplina extends IdentityGeneratorIdentifierEntity<Long> implements Serializable {
	
	
	@Column
	private String nome;
	
	@Lob
	@Column(length = 300)
	private String descricao;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "disciplina",
			orphanRemoval = false)
	private Set<Post> posts = new HashSet<>(0);
	
	@Override
	public Optional populateForCache() {	
		return Optional.empty();
	}

}
