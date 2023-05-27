package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = "post")
@ToString(exclude = "post")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class Tag extends IdentityGeneratorIdentifierEntity<Long> implements Serializable {
	
	@Column
	@Size(max = 12)
	@NotEmpty
	private String nome;
	
	@Column
	private Integer qtdePosts;
	
	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
			mappedBy = "tags")
	private Set<Post> post = new HashSet<>(0);	

	public void setNome(String value) {
		this.nome = value.trim();
	}
	
}
