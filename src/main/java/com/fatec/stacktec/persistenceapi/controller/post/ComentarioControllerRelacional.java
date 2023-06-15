package com.fatec.stacktec.persistenceapi.controller.post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fatec.stacktec.persistenceapi.dto.post.ComentarioDto;
import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.model.util.BaseModel;
import com.fatec.stacktec.persistenceapi.service.post.ComentarioService;
import com.fatec.stacktec.persistenceapi.service.post.PostService;
import com.fatec.stacktec.persistenceapi.service.post.RespostaService;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@CrossOrigin
@RestController
@RequestMapping("/api/comentario")
@Api(value = "ComentarioRelacional", description = "comentário api", tags = {"Comentário"})
@Validated
public class ComentarioControllerRelacional extends BaseController<ComentarioService, Comentario, ComentarioDto>{
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Autowired
	private UserInternalService userService;
	
	@ApiOperation(value = "Get comentário by id")
	@GetMapping("/v1.1/{id}")
	@Transactional(readOnly = true)
	public ResponseEntity getComentarioById(@PathVariable(value = "id") Long elementId) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Comentario element = (Comentario) service.findById(elementId);
		if(element != null) {
			return ResponseEntity.ok(convertToDetailDto(element));
		}		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Upload comentário")
	@PostMapping("/v1.1/create")
	@Transactional
	public ResponseEntity createElement(@Valid @RequestBody ComentarioDto comentarioDto) {
		
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Comentario converted = convertToModel(comentarioDto);
		Comentario elementCreated = (Comentario) service.createElement(converted);
		if(elementCreated != null) {						
			ObjectNode response = objectMapper.createObjectNode();								
			response.put("id", ((BaseModel<Long>) (elementCreated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Update comentário")
	@PutMapping("/v1.1/update/{id}")
	@Transactional
	public ResponseEntity updatePost(@PathVariable(value = "id") Long comentarioId, @Valid @RequestBody ComentarioDto comentarioDto) {
				
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Comentario converted = service.updateComentario(modelMapper, comentarioDto);
		Comentario elementUpdated = (Comentario) service.updateElement(comentarioId, converted);
		if(elementUpdated != null) {						
			ObjectNode response = objectMapper.createObjectNode();								
			response.put("id", ((BaseModel<Long>) (elementUpdated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
		
	
	
	@Override
	protected List<?> convertToListDto(List<Comentario> comentarioList) {
		if(comentarioList != null && !comentarioList.isEmpty()) {
			return comentarioList.stream().map(this::convertToDetailDto).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	protected Object convertToDetailDto(Comentario comentario) {
		UserInternal autor = null;
		Post post = null;
		Resposta resposta = null;
		
		if(comentario.getAutor() != null) {
			autor = comentario.getAutor();
			comentario.setAutor(null);
		}
		if(comentario.getPost() != null) {
			post = comentario.getPost();
			comentario.setPost(null);
		}
		if(comentario.getResposta() != null) {
			resposta = comentario.getResposta();
			comentario.setResposta(null);
		}
		
		ComentarioDto comentarioDto = modelMapper.map(resposta, ComentarioDto.class);
		if(autor != null) {
			comentarioDto.setAutor(autor.getId());
		}
		if(post != null) {
			comentarioDto.setPostId(post.getId());
		}
		if(autor != null) {
			comentarioDto.setRespostaId(resposta.getId());
		}
		
		return comentarioDto;
	}

	@Override
	@Transactional
	protected Comentario convertToModel(ComentarioDto dto) {
		Comentario comentario = modelMapper.map(dto, Comentario.class);
		
		if(dto.getPostId() != null) {
			Post post = null;
			post = postService.getOne(dto.getPostId());
			if(post != null)
				comentario.setPost(post);
		}
		
		//Define o atual usuário logado como autor
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
		UserInternal user = userService.findByEmail(name);		
		if(user != null)
			comentario.setAutor(user);	
				
		
		
		if(dto.getRespostaId() != null) {
			Resposta resposta = null;
			resposta = respostaService.getOne(dto.getRespostaId());
			if(resposta != null)
				comentario.setResposta(resposta);
		}			
		
		return comentario;
	}
	

}
