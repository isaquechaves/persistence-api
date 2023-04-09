package com.fatec.stacktec.persistenceapi.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;

@Repository
public interface UserInternalRepository extends JpaRepository<UserInternal, Long> {
	
	@Query("from UserInternal where lower(userSite.email) like lower(:email) ")
	Optional<UserInternal> findByEmail(String email);		
    Boolean existsByEmail(String email);

}
