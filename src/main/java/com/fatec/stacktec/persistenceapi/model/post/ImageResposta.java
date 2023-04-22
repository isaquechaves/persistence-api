package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "resposta_x_image")
public class ImageResposta extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
	
	@Column
	private long imageId;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resposta_id", foreignKey = @ForeignKey(name = "fk_imageresposta_resposta"))
	private Resposta resposta;
	
	@Override
	public Optional populateForCache() {
		return Optional.empty();
	}
}
