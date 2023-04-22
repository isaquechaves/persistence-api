package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comentario")
//@EntityListeners
public class Comentario extends IdentityGeneratorIdentifierEntity<Long> implements Serializable {
	
	@NotNull
	private UserInternal autor;

	@NotNull
	@Lob
	@Column(length = 500)
	private String texto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_comentario_post"))
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resposta_id", foreignKey = @ForeignKey(name = "fk_comentario_resposta"))
	private Resposta resposta;
	
	@Override
	public Optional populateForCache() {		
		return Optional.empty();
	}
	
}
