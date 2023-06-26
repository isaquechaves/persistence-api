package com.fatec.stacktec.persistenceapi.service.user;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fatec.stacktec.persistenceapi.dto.UserInternalPageDto;
import com.fatec.stacktec.persistenceapi.dto.UserInternalDto;
import com.fatec.stacktec.persistenceapi.dto.UserInternalDtoMinimal;
import com.fatec.stacktec.persistenceapi.dto.UserInternalMinimalToPageDto;
import com.fatec.stacktec.persistenceapi.model.user.Role;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.repository.user.UserInternalRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import antlr.StringUtils;

@Service
public class UserInternalService extends CrudServiceJpaImpl<UserInternalRepository, UserInternal> implements UserDetailsService{
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private RoleService roleService;
	
	@Transactional
	public UserInternal findByIdAllRelationships(Long id) {
		UserInternal user = repository.getOne(id);
		if(user.getRoles() != null)
			user.getRoles().size();
		return user;		
	}
	
	public UserInternal findByEmail(String email) {
		return repository.findByEmail(email.toLowerCase()).
				orElseThrow(() ->
					new UsernameNotFoundException("User not found with or email: "+ email));
	}
	
	public UserInternal createElement(UserInternal o) {
		return this.repository.save(o);
	}
	
	@Transactional
	public UserInternal createElementAndFlush(UserInternal o) {
		return this.repository.save(o);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserInternal user = repository.findByEmail(email)
	                 .orElseThrow(() ->
	                         new UsernameNotFoundException("User not found with or email: "+ email));

	        Set<GrantedAuthority> authorities = user
	                .getRoles()
	                .stream()
	                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

	        return new User(user.getEmail(),
	                user.getPassword(),
	                authorities);
	}

	public Optional<UserInternal> getOneById(Long autorId) {
		return repository.getById(autorId);
	}

	public UserInternalPageDto findUsersByEmailPaginated(ModelMapper modelMapper, Integer pageNumber,
			Integer pageSize, String email) {
		String jpql = "SELECT u FROM UserInternal u LEFT JOIN FETCH u.roles  WHERE u.email like CONCAT('%', :email, '%') ORDER BY u.id ASC";
		String countJpql = "SELECT COUNT(u) FROM UserInternal u WHERE u.email like CONCAT('%', :email, '%')";
	
	    
	    TypedQuery<UserInternal> query = entityManager.createQuery(jpql, UserInternal.class);	    	  
    	query.setParameter("email", email);
	    
	    query.setFirstResult((pageNumber - 1) * pageSize);
	    query.setMaxResults(pageSize);
	    
	    List<UserInternal> users = query.getResultList();
	    Map<UserInternal, List<String>> userRolesMap = new LinkedHashMap<>();
	    for (UserInternal user : users) {
	        List<String> roleNames = user.getRoles().stream()
	                .map(Role::getName)
	                .collect(Collectors.toList());
	        userRolesMap.put(user, roleNames);
	    }

	    List<UserInternalMinimalToPageDto> usersMinimalsDtos = userRolesMap.entrySet().stream()
	            .map(entry -> {
	                UserInternal user = entry.getKey();
	                List<String> roleNames = entry.getValue();
	                UserInternalMinimalToPageDto userDto = modelMapper.map(user, UserInternalMinimalToPageDto.class);
	                userDto.setRoles(roleNames);
	                return userDto;
	            })
	            .collect(Collectors.toList());
	    
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
	    countQuery.setParameter("email", email);
	       
	    Long total = countQuery.getSingleResult();
	    Integer totalPages = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalResults = total.intValue();
	    
		return new UserInternalPageDto(totalPages, totalResults, usersMinimalsDtos);
	}
	
	public UserInternalPageDto findAllUsersPaginated(ModelMapper modelMapper, Integer pageNumber,
			Integer pageSize) {	
		String jpql = "SELECT u FROM UserInternal u  LEFT JOIN FETCH u.roles  ORDER BY u.id ASC";
		String countJpql = "SELECT COUNT(u) FROM UserInternal u";
		
	    
	    TypedQuery<UserInternal> query = entityManager.createQuery(jpql, UserInternal.class);	    	 
	    
	    query.setFirstResult((pageNumber - 1) * pageSize);
	    query.setMaxResults(pageSize);
	    
	    List<UserInternal> users = query.getResultList();
	    Map<UserInternal, List<String>> userRolesMap = new LinkedHashMap<>();
	    for (UserInternal user : users) {
	        List<String> roleNames = user.getRoles().stream()
	                .map(Role::getName)
	                .collect(Collectors.toList());
	        userRolesMap.put(user, roleNames);
	    }

	    List<UserInternalMinimalToPageDto> usersMinimalsDtos = userRolesMap.entrySet().stream()
	            .map(entry -> {
	                UserInternal user = entry.getKey();
	                List<String> roleNames = entry.getValue();
	                UserInternalMinimalToPageDto userDto = modelMapper.map(user, UserInternalMinimalToPageDto.class);
	                userDto.setRoles(roleNames);
	                return userDto;
	            })
	            .collect(Collectors.toList());
	    
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);	    
	    Long total = countQuery.getSingleResult();
	    Integer totalPages = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalResults = total.intValue();
	    
		return new UserInternalPageDto(totalPages, totalResults, usersMinimalsDtos);
	}


	@Transactional
	public UserInternal updateUsuario(ModelMapper modelMapper, Long id, UserInternalDto dto) {
		UserInternal user = findById(id);
		if(user!= null) {
			if(user.getEmail().equals(dto.getEmail())) {
				user.setApelido(dto.getApelido());
					
				Set<Role> rolesUser = new HashSet<>();
				rolesUser.add(roleService.findByName("ROLE_ALUNO"));
				user.setRoles(rolesUser);
				user.setName(dto.getName());
				user.setSemestre(dto.getSemestre());
				UserInternal userUpdated = updateElement(id, user);			
				return userUpdated;
			}else {
				throw new UsernameNotFoundException("Cannot change user email: ");
			}
		}
		return null;
	}
	
	@Transactional
	public UserInternal updateUsuarioByAdmin(ModelMapper modelMapper, Long id, UserInternalDto dto) {
		UserInternal user = findById(id);
		Optional<UserInternal> testEmail = repository.findByEmail(dto.getEmail().toLowerCase());
		
		user.setApelido(dto.getApelido());
		if(!testEmail.isPresent()) {
			user.setEmail(dto.getEmail());
		}
		Set<Role> rolesUser = new HashSet<>();
		for(String role : dto.getRoles()) {
			rolesUser.add(roleService.findByName(role));
		}		
		user.setRoles(rolesUser);
		user.setName(dto.getName());
		user.setSemestre(dto.getSemestre());
		UserInternal userUpdated = updateElement(id, user);
		return userUpdated;
	}
			
}

