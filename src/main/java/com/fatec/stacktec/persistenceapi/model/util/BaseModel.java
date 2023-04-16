package com.fatec.stacktec.persistenceapi.model.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel<ID> implements Serializable {
	
	public abstract ID getId();
	public abstract void setId(ID id);
	
	@Version
	@ApiModelProperty(hidden = true)
	protected Integer version;
	
	@Column
	@CreatedBy
	@ApiModelProperty(hidden = true)
	protected String createdBy;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@ApiModelProperty(hidden = true)
	protected Date createdAt;
	
	@Column	
	@LastModifiedBy
	@ApiModelProperty(hidden = true)
	protected String UpdatedBy;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@ApiModelProperty(hidden = true)
	protected Date UpdatedAt;
			
	
	@Transient
	public abstract Optional populateForCache();

}
