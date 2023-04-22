package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"images", "comentarios"})
@ToString(exclude = {"images", "comentarios"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resposta")
//@EntityListeners
public class Resposta extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
		
	@NotNull
	@Column
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String descricao;
		
	@Column
	private UserInternal autor;
	
	@Column
	private Integer upvotes;
	
	@Column
	private Boolean aceita;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_resposta_post"))
	private Post post;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "resposta",
			orphanRemoval = true)
	private Set<ImageResposta> images = new HashSet<>(0);
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "resposta",
			orphanRemoval = true)
	private Set<Comentario> comentarios = new HashSet<>(0);

	@Column
	@CreatedDate
	private Date criadoEm;
	
	@Column
	@UpdateTimestamp
	private Date atualizadoEm;

	@Override
	public Optional populateForCache() {
		return Optional.empty();
	}
}
