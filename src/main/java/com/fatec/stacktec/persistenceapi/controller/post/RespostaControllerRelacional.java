package com.fatec.stacktec.persistenceapi.controller.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fatec.stacktec.persistenceapi.controller.BaseController;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaDto;
import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.model.util.BaseModel;
import com.fatec.stacktec.persistenceapi.service.post.ComentarioService;
import com.fatec.stacktec.persistenceapi.service.post.DisciplinaService;
import com.fatec.stacktec.persistenceapi.service.post.PostService;
import com.fatec.stacktec.persistenceapi.service.post.RespostaService;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@CrossOrigin
@RestController
@RequestMapping("/api/resposta")
@Api(value = "RespostaRelacional", description = "resposta api", tags = {"Resposta"})
@Validated
public class RespostaControllerRelacional extends BaseController<RespostaService, Resposta, RespostaDto>{
	
	private final PostService postService;

	private final RespostaService respostaService;
	
	private final ComentarioService comentarioService;
		
	private final UserInternalService userService;	
	
	
	public RespostaControllerRelacional(PostService postService, RespostaService respostaService, 
			ComentarioService comentarioService,
			UserInternalService userService) {
		this.postService = postService;
		this.respostaService = respostaService;		
		this.comentarioService = comentarioService;		
		this.userService = userService;			
	}
	
	
	@ApiOperation(value = "Get resposta by id")
	@GetMapping("/v1.1/{id}")
	@Transactional(readOnly = true)
	public ResponseEntity getRespostaById(@PathVariable(value = "id") Long elementId) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Resposta element = (Resposta) service.findById(elementId);
		if(element != null) {
			return ResponseEntity.ok(convertToDetailDto(element));
		}		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Create resposta")
	@PostMapping("/v1.1/create")
	@Transactional
	public ResponseEntity createElement(@Valid @RequestBody RespostaDto respostaDto) {
				
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Resposta converted = convertToModel(respostaDto);
		Resposta elementCreated = (Resposta) respostaService.createElement(converted);
		if(elementCreated != null) {						
			ObjectNode response = objectMapper.createObjectNode();								
			response.put("id", ((BaseModel<Long>) (elementCreated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Update resposta")
	@PutMapping("/v1.1/update/{id}")
	@Transactional
	public ResponseEntity updateResposta(@PathVariable(value = "id") Long respostaId, @Valid @RequestBody RespostaDto respostaDto) {
				
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Resposta converted = respostaService.updateResposta(modelMapper, respostaDto);
		Resposta elementUpdated = (Resposta) respostaService.updateElement(respostaId, converted);
		if(elementUpdated != null) {						
			ObjectNode response = objectMapper.createObjectNode();								
			response.put("id", ((BaseModel<Long>) (elementUpdated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
		


	@Override
	protected List<?> convertToListDto(List<Resposta> respostaList) {
		if(respostaList != null && !respostaList.isEmpty()) {
			return respostaList.stream().map(this::convertToDetailDto).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}


	@Override
	protected Object convertToDetailDto(Resposta resposta) {		
		UserInternal autor = null;
		Post post = null;
		
		if(resposta.getAutor() != null) {
			autor = resposta.getAutor();
			resposta.setAutor(null);
		}	
		
		if(resposta.getPost() != null) {
			post = resposta.getPost();
			resposta.setPost(null);
		}			
		
		if(resposta.getComentarios() != null) {
			Set<Comentario> listComentario = new HashSet<>(0);
			for(Comentario comentario: resposta.getComentarios()) {
				listComentario.add(comentario);
			}
			resposta.setComentarios(listComentario);
		}
		
		RespostaDto respostaDto = modelMapper.map(resposta, RespostaDto.class);								
		if(autor != null) {
			respostaDto.setAutorId(autor.getId());
		}	
		
		if(post != null) {
			respostaDto.setPostId(post.getId());
		}	
		return respostaDto;
	}	

	@Override
	@Transactional
	protected Resposta convertToModel(RespostaDto dto) {		
				    
		Resposta resposta = modelMapper.map(dto, Resposta.class);				
		Set<Comentario> comentariosResposta = new HashSet<>(0);
		
		//Define o atual usuário logado como autor
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
		UserInternal user = userService.findByEmail(name);		
		if(user != null)
			resposta.setAutor(user);	
		
		if(dto.getPostId() != null) {
			Post post = null;
			post = postService.getOne(dto.getPostId());
			if(post != null)
				resposta.setPost(post);
		}		
		
		if(resposta.getComentarios() != null) {
			comentariosResposta = resposta.getComentarios();
		}									
		
		Set<RespostaComentarioDto> comentarioDtoList = dto.getComentarios();
		dto.setComentarios(null);
		
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
		
		return resposta;
	}	
		

}
