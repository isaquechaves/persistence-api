package com.fatec.stacktec.persistenceapi.service.post;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.repository.post.RespostaRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class RespostaService extends CrudServiceJpaImpl<RespostaRepository, Resposta>{

}
