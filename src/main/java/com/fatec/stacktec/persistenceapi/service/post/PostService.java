package com.fatec.stacktec.persistenceapi.service.post;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.stacktec.persistenceapi.dto.post.PostComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaDto;
import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.model.post.Disciplina;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.model.post.Tag;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.repository.post.PostRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import lombok.extern.java.Log;

@Log
@Service
public class PostService extends CrudServiceJpaImpl<PostRepository, Post>{
	
	
	@Autowired
	private DisciplinaService disciplinaService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ComentarioService comentarioService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Autowired
	private UserInternalService userService;
	
	@Transactional
	public Post updatePost(ModelMapper modelMapper, PostDto dto) {
		Post post = modelMapper.map(dto, Post.class);		
		Set<Resposta> respostasPost = new HashSet<>(0);
		Set<Tag> tagsPost = new HashSet<>(0);
		Set<Comentario> comentariosPost = new HashSet<>(0);		
		
		if(post.getRespostas() != null) {
			respostasPost = post.getRespostas();
		}
		
		if(post.getTags() != null) {
			tagsPost = post.getTags();
		}
		
		if(post.getComentarios() != null) {
			comentariosPost = post.getComentarios();
		}
		
		
		tagsPost = findTagsByName(dto.getTags());		
		post.setTags(tagsPost);
		
		if(dto.getDisciplinaId() != null) {
			Optional<Disciplina> disciplina = null;
			disciplina =  disciplinaService.getById(dto.getDisciplinaId());
			if(disciplina.isPresent()) {				
				post.setDisciplina(disciplina.get());
			}			
		}
						
		
		Set<RespostaDto> respostaDtoList = dto.getRespostas();
		dto.setRespostas(null);
		
		if(respostaDtoList != null) {
			Set<Resposta> respostaList = new HashSet<>(0);
			for(RespostaDto respostaDto : respostaDtoList) {
				Resposta resposta = respostaService.findById(respostaDto.getId());
				if(resposta != null) {
					respostaList.add(resposta);
				}
			}
			if(respostasPost != null) {
				respostasPost.clear();
				respostasPost.addAll(respostaList);
			}else {
				respostasPost = respostaList;
			}
		}else {
			if(respostasPost != null) {
				respostasPost.clear();
			}else {
				respostasPost = new HashSet<>(0);
			}
		}
		post.setRespostas(respostasPost);
		
		
		Set<PostComentarioDto> comentarioDtoList = dto.getComentarios();
		dto.setComentarios(null);
		
		if(comentarioDtoList != null) {
			Set<Comentario> comentarioList = new HashSet<>(0);
			for(PostComentarioDto comentarioDto : comentarioDtoList) {
				Comentario comentario = comentarioService.findById(comentarioDto.getId());
				if(comentario != null) {
					comentarioList.add(comentario);
				}
			}
			if(comentariosPost != null) {
				comentariosPost.clear();
				comentariosPost.addAll(comentarioList);
			}else {
				comentariosPost = comentarioList;
			}
		}else {
			if(comentariosPost != null) {
				comentariosPost.clear();
			}else {
				comentariosPost = new HashSet<>(0);
			}
		}
		post.setComentarios(comentariosPost);
		
		
		if(dto.getAutorId() != null) {
			Optional<UserInternal> user = userService.getOneById(dto.getAutorId());		
			if(user.isPresent()) {
				post.setAutor(user.get());
			}
		}
		return post;
	}

	public Set<Tag> findTagsByName(Set<String> nomes) {
		Set<Tag> tagList = new HashSet<>();
		
		for(String nome: nomes) {
			Optional<Tag> tagOptional = tagService.getByNome(nome);
			if(tagOptional.isPresent()) {
				//Incrementa em um a quantidade de posts na Tag encontrada
				tagOptional.get().setQtdePosts(tagOptional.get().getQtdePosts()+1);
				tagService.updateElement(tagOptional.get().getId(), tagOptional.get());
				tagList.add(tagOptional.get());
			}
			else {
				Tag tagToCreate = new Tag();
				tagToCreate.setNome(nome);
				tagService.createElement(tagToCreate);
				Optional<Tag> tagCreatedOptional = tagService.getByNome(nome);
				if(tagCreatedOptional.isPresent()) {
					//Incrementa em um a quantidade de posts na Tag criada
					Tag tagCreated = tagService.getOne(tagCreatedOptional.get().getId());
					tagCreated.setQtdePosts(1);
					tagService.updateElement(tagCreated.getId(), tagCreated);
					tagList.add(tagCreatedOptional.get());
				}
			}
		}
				
		return tagList;
	}

	public Optional<Post> getById(Long id) {
		return repository.getById(id);
	}
	
}
