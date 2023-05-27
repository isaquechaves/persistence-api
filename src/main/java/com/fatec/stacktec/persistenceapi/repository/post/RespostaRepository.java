package com.fatec.stacktec.persistenceapi.repository.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Resposta;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long>{

	Optional<Resposta> getById(@Param("id") Long id);

}
