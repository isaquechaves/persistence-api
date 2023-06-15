package com.fatec.stacktec.persistenceapi.service.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.dto.post.ComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaDto;
import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.repository.post.ComentarioRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import lombok.extern.java.Log;

@Log
@Service
public class ComentarioService extends CrudServiceJpaImpl<ComentarioRepository, Comentario>{
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Autowired
	private UserInternalService userService;
	
	public Comentario updateComentario(ModelMapper modelMapper, @Valid ComentarioDto comentarioDto) {
		Comentario comentario = modelMapper.map(comentarioDto, Comentario.class);
		
		if(comentarioDto.getPostId() != null) {
			Optional<Post> post = null;
			post = postService.getById(comentarioDto.getPostId());
			if(post.isPresent())
				comentario.setPost(post.get());
		}
		
		if(comentarioDto.getRespostaId() != null) {
			Optional<Resposta> resposta = null;
			resposta = respostaService.getById(comentarioDto.getRespostaId());
			if(resposta.isPresent())
				comentario.setResposta(resposta.get());
		}
		
		
		if(comentarioDto.getAutor() != null) {
			Optional<UserInternal> user = userService.getOneById(comentarioDto.getAutor());
			if(user.isPresent())
				comentario.setAutor(user.get());
		}
		
		
		return comentario;
	}
	
	public Optional<Comentario> getById(Long id){
		return repository.getById(id);
	}

	public static List<RespostaComentarioDto> mapComentariosDtoParaRespostas(List<Comentario> comentarios, ModelMapper modelMapper,
			UserInternalService userService) {	
		List<RespostaComentarioDto> comentariosDtos  = new ArrayList<>();
		for(Comentario comentario : comentarios) {
			UserInternal autor = comentario.getAutor();
			comentario.setAutor(null);
			RespostaComentarioDto comentarioResposta = modelMapper.map(comentario, RespostaComentarioDto.class);			
			comentarioResposta.setAutorApelido(autor.getApelido());	
			comentarioResposta.setAutor(autor.getId());
			comentariosDtos.add(comentarioResposta);
		}
		return comentariosDtos;
	}
	
	public static List<PostComentarioDto> mapComentariosDtoParaPosts(List<Comentario> comentarios, ModelMapper modelMapper,
			UserInternalService userService) {	
		List<PostComentarioDto> comentariosDtos  = new ArrayList<>();
		for(Comentario comentario : comentarios) {
			UserInternal autor = comentario.getAutor();
			comentario.setAutor(null);
			PostComentarioDto comentarioPost = modelMapper.map(comentario, PostComentarioDto.class);
			comentarioPost.setAutorApelido(autor.getApelido());	
			comentarioPost.setAutorId(autor.getId());	
			comentariosDtos.add(comentarioPost);
		}
		return comentariosDtos;
	}
}
