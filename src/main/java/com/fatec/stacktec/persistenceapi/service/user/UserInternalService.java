package com.fatec.stacktec.persistenceapi.service.user;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.repository.user.UserInternalRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

@Service
public class UserInternalService extends CrudServiceJpaImpl<UserInternalRepository, UserInternal> implements UserDetailsService{
	
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
}
