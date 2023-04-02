package com.fatec.stacktec.persistenceapi.model.user;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fatec.stacktec.persistenceapi.enumeration.GenderType;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;
import com.fatec.stacktec.searchapi.util.ImgData;

import lombok.Data;

@Data
@Entity
@Table(name = "usersite")
@EntityListeners({KeycloakUserSiteEntityListener.class})
public class UserSite extends IdentityGeneratorIdentifierEntity<Long> implements Serializable {
	
	@OneToOne(mappedBy = "userSite", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private UserInternal userInternal;
	
	@Column
	private String name;
	
	@NotNull
	@Column	
	private String email;
	
	@Column
	private String apelido;
	
	@Column
	private ImgData image;
	
	@Column
	private GenderType gender;
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date birthDate;
	
	@Column
	private String keycloakId;
	
	@Column
	private String password;
	
	@Transient
	@Override
	public Optional populateForCache() {
		return Optional.empty();
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof UserSite)) return false;
		if(!super.equals(o)) return false;
		UserSite userSite = (UserSite) o;
		return Objects.equals(getId(), userSite.getId()) &&
				Objects.equals(getEmail(), userSite.getEmail());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getEmail());
	}
	
	@Override
	public String toString() {
		return "UserSite{" +
			"id=" + getId() +
			", name='" + name + '\'' +
			", email='" + email + '\'' +
			", gender='" + gender + '\'' +
			", birthDate='" + birthDate + '\'' +
			", keycloakId='" + keycloakId + '\'' +
			", password='" + password + '\'' +
			'}';		
	}
}
