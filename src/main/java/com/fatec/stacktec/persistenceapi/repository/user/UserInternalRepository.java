package com.fatec.stacktec.persistenceapi.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.user.UserInternal;

@Repository
public interface UserInternalRepository extends JpaRepository<UserInternal, Long> {
	
	Optional<UserInternal> getById(@Param("id") Long id);
	
	@Query("from UserInternal where lower(email) like lower(:email) ")
	Optional<UserInternal> findByEmail(String email);		
    Boolean existsByEmail(String email);

}
