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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fatec.stacktec.persistenceapi.listener.user.UserInternalEntityListener;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;
import com.fatec.stacktec.searchapi.enumeration.SemestreType;
import com.fatec.stacktec.searchapi.holder.UserInternalHolder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false, exclude = {"roles", "posts"})
@NoArgsConstructor // Add this annotation
@ToString(exclude = {"roles", "posts"})
@Entity
@Table(name = "userInternal")
@EntityListeners({UserInternalEntityListener.class})
public class UserInternal extends IdentityGeneratorIdentifierEntity<Long> implements Serializable {
	
	@Column(nullable = false, columnDefinition = "boolean default true")
	boolean enabled = true;
	
	@ManyToMany(
			fetch = FetchType.EAGER,
			cascade = {CascadeType.MERGE},
			targetEntity = Role.class)
	@JoinTable(name = "userinternal_x_role",
			joinColumns = {@JoinColumn(name = "userinternal_id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id")},
			foreignKey = @ForeignKey(name = "fk_userinternal_role"))
	@Fetch(FetchMode.SUBSELECT)
	private Set<Role> roles = new HashSet<>(0);
	
	@Column	
	private String apelido;
	
	@Column
	private String name;
	
	@Column
    private String email;
	
	@Column
    private String password;
	
	@Column	
	private SemestreType semestre;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			fetch = FetchType.LAZY, mappedBy = "autor",
			orphanRemoval = true)
	private Set<Post> posts = new HashSet<>(0);
	
	public UserInternal(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public Optional<UserInternalHolder> populateForCache() {
		return Optional.of(new UserInternalHolder(getId(), getName(), getEmail(), getApelido(), getSemestre()));
	}
	
	
}
