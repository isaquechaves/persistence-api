package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, exclude = {"posts"})
@ToString(exclude = {"posts"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "disciplina")
public class Disciplina extends IdentityGeneratorIdentifierEntity<Long> implements Serializable {
	
	
	@Column
	private String nome;
	
	@Lob
	@Column(length = 300)
	@Type(type = "org.hibernate.type.TextType")
	private String descricao;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "disciplina",
			orphanRemoval = false)
	@JsonIgnore
	private Set<Post> posts = new HashSet<>(0);

}
