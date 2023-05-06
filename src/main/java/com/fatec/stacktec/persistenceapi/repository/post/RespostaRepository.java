package com.fatec.stacktec.persistenceapi.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Resposta;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long>{

}
