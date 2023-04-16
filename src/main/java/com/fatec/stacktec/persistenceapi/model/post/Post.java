package com.fatec.stacktec.persistenceapi.model.post;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


//@EntityListeners
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{
	
	@Column
	@Lob
	private String titulo;
	
	@Column
	@Lob
	private String descricao;
	
	@Column
	@CreatedDate
	private Date criadoEm;
	
	@Column
	@UpdateTimestamp
	private Date atualizadoEm;

	@Override
	public Optional populateForCache() {
		// TODO Auto-generated method stub
		return null;
	}
}
