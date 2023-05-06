package com.fatec.stacktec.persistenceapi.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{

}
