package com.fatec.stacktec.persistenceapi.service.post;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.post.Disciplina;
import com.fatec.stacktec.persistenceapi.repository.post.DisciplinaRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class DisciplinaService extends CrudServiceJpaImpl<DisciplinaRepository, Disciplina>{

}
