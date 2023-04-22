package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "post_x_image")
public class ImagePost extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
	
	@Column
	private long imageId;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_imagepost_post"))
	private Post post;
	
	@Override
	public Optional populateForCache() {
		return Optional.empty();
	}

}
