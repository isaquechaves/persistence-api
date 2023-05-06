package com.fatec.stacktec.persistenceapi.service.post;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.repository.post.ComentarioRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class ComentarioService extends CrudServiceJpaImpl<ComentarioRepository, Comentario>{

}
