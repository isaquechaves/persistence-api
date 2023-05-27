package com.fatec.stacktec.persistenceapi.repository.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{

	Optional<Comentario> getById(@Param("id") Long id);

}
