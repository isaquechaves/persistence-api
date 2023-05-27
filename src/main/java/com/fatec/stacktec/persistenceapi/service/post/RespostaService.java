package com.fatec.stacktec.persistenceapi.service.post;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.stacktec.persistenceapi.dto.post.PostComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaDto;
import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.repository.post.RespostaRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import lombok.extern.java.Log;

@Log
@Service
public class RespostaService extends CrudServiceJpaImpl<RespostaRepository, Resposta>{
		
	@Autowired
	private ComentarioService comentarioService;
	
	@Autowired
	private UserInternalService userService;
	
	@Autowired
	private PostService postService;
	
	
	@Transactional
	public Resposta updateResposta(ModelMapper modelMapper, @Valid RespostaDto respostaDto) {
		Resposta resposta = modelMapper.map(respostaDto, Resposta.class);
		Set<Comentario> comentariosResposta = new HashSet<>(0);
		
		if(resposta.getComentarios() != null) {
			comentariosResposta = resposta.getComentarios();
		}
		
		Set<RespostaComentarioDto> comentarioDtoList = respostaDto.getComentarios();
		respostaDto.setComentarios(null);
		
		if(comentarioDtoList != null) {
			Set<Comentario> comentarioList = new HashSet<>(0);
			for(RespostaComentarioDto comentarioDto : comentarioDtoList) {
				Comentario comentario = comentarioService.findById(comentarioDto.getId());
				if(comentario != null) {
					comentarioList.add(comentario);
				}
			}
			if(comentariosResposta != null) {
				comentariosResposta.clear();
				comentariosResposta.addAll(comentarioList);
			}else {
				comentariosResposta = comentarioList;
			}
		}else {
			if(comentariosResposta != null) {
				comentariosResposta.clear();
			}else {
				comentariosResposta = new HashSet<>(0);
			}
		}		
		resposta.setComentarios(comentariosResposta);
		
		if(respostaDto.getPostId() != null) {
			Optional<Post> post = null;
			post = postService.getById(respostaDto.getPostId());
			if(post.isPresent())
				resposta.setPost(post.get());
		}
		
		
		if(respostaDto.getAutorId() != null) {
			Optional<UserInternal> user = userService.getOneById(respostaDto.getAutorId());
			if(user.isPresent())
				resposta.setAutor(user.get());
		}
		
		return resposta;
	}


	public Optional<Resposta> getById(Long id) {
		return repository.getById(id);
	}

}
