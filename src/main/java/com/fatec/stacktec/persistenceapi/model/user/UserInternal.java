package com.fatec.stacktec.persistenceapi.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fatec.stacktec.persistenceapi.enumeration.Apps;
import com.fatec.stacktec.persistenceapi.model.util.NoGeneratorIdentifierEntity;
import com.fatec.stacktec.searchapi.enumeration.SemestreType;
import com.fatec.stacktec.searchapi.holder.UserInternalHolder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"userSite", "permission", "apps"})
@ToString(exclude = {"userSite", "permission", "apps"})
@Entity
@Table(name = "userInternal")
@EntityListeners({UserInternalEntityListener.class, KeycloakUserInternalEntityListener.class})
public class UserInternal extends NoGeneratorIdentifierEntity<Long> implements Serializable {
	
	@Column(nullable = false, columnDefinition = "boolean default true")
	boolean enabled = true;
	
	@ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			targetEntity = Permission.class)
	@JoinTable(name = "userinternal_x_permission",
			joinColumns = {@JoinColumn(name = "userinternal_id")},
			inverseJoinColumns = {@JoinColumn(name = "permission_id")},
			foreignKey = @ForeignKey(name = "fk_userinternal_permission"))
	private Set<Permission> permission = new HashSet<>(0);
	
	@NotNull
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST}, optional = true)
	@PrimaryKeyJoinColumn(referencedColumnName = "id", name = "id", foreignKey = @ForeignKey(name = "fk_userinternal_usersite"))
	@Fetch(FetchMode.JOIN)
	private UserSite userSite;
	
	@Column	
	private String apelido;
	
	@Column	
	private SemestreType semestre;
		
	@Transient
	private Long userSiteId;
	
	@ElementCollection(targetClass = Apps.class, fetch = FetchType.LAZY)
	@CollectionTable(name = "userinternal_x_apps", foreignKey = @ForeignKey(name = "fk_userinternal_apps"), joinColumns = @JoinColumn(name = "userinternal_id"))
	@Column(name = "apps")
	@Fetch(FetchMode.SUBSELECT)
	private Set<Apps> apps;
	
	private Long getUserSiteId() {
		return userSite != null ? userSite.getId() : null;
	}
	
	@Override
	public Optional populateForCache() {
		String keycloakId = getUserSite() != null ? getUserSite().getKeycloakId() : null;
		return Optional.of(new UserInternalHolder(getId(), getUserSite().getName(), keycloakId, userSite.getEmail(), userSite.getApelido(), semestre));
	}		
	
}
