package com.fatec.stacktec.persistenceapi.model.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import com.fatec.stacktec.persistenceapi.listener.keycloak.KeycloakRolesEntityListener;
import com.fatec.stacktec.persistenceapi.model.util.IdentityGeneratorIdentifierEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "permission")
@EqualsAndHashCode(callSuper = true, exclude = "userInternal")
@EntityListeners({PermissionEntityListener.class, KeycloakRolesEntityListener.class})
public class Permission extends IdentityGeneratorIdentifierEntity<Long> implements Serializable{

}
