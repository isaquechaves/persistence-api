package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;



//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
//@EntityListeners
public class Post extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
	
	@NotNull
	@Column
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String titulo;
	
	@NotNull
	@Column
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String descricao;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_autor_post"))
	private UserInternal autor;	
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "post",
			orphanRemoval = true)
	private Set<Resposta> respostas = new HashSet<>(0);
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "post",
			orphanRemoval = true)
	private Set<Comentario> comentarios = new HashSet<>(0);
	
	@ManyToMany(
			fetch = FetchType.EAGER,
			cascade = {CascadeType.MERGE},
			targetEntity = Tag.class)
	@JoinTable(name = "post_x_tag",
			joinColumns = {@JoinColumn(name = "id")},
			inverseJoinColumns = {@JoinColumn(name = "tag_id")},
			foreignKey = @ForeignKey(name = "fk_post_tag"))
	@Fetch(FetchMode.SUBSELECT)
	private Set<Tag> tags = new HashSet<>(0);	 	
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disciplina_id", foreignKey = @ForeignKey(name = "fk_disciplina_post"))
	private Disciplina disciplina;
		
	@Column
	@CreatedDate
	private Date criadoEm;
	
	@Column
	@UpdateTimestamp
	private Date atualizadoEm;

}
