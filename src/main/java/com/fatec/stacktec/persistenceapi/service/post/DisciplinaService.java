package com.fatec.stacktec.persistenceapi.service.post;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.post.Disciplina;
import com.fatec.stacktec.persistenceapi.repository.post.DisciplinaRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class DisciplinaService extends CrudServiceJpaImpl<DisciplinaRepository, Disciplina>{
	
	public Optional<Disciplina> getById(Long id) {
		return repository.getById(id);
	}
}
