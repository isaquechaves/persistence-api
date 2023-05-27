package com.fatec.stacktec.persistenceapi.controller.post;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.fatec.stacktec.persistenceapi.dto.post.PostComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostDto;
import com.fatec.stacktec.persistenceapi.dto.post.RespostaDto;
import com.fatec.stacktec.persistenceapi.dto.post.TagDto;
import com.fatec.stacktec.persistenceapi.model.post.Comentario;
import com.fatec.stacktec.persistenceapi.model.post.Disciplina;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Resposta;
import com.fatec.stacktec.persistenceapi.model.post.Tag;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.model.util.BaseModel;
import com.fatec.stacktec.persistenceapi.service.post.ComentarioService;
import com.fatec.stacktec.persistenceapi.service.post.DisciplinaService;
import com.fatec.stacktec.persistenceapi.service.post.PostService;
import com.fatec.stacktec.persistenceapi.service.post.RespostaService;
import com.fatec.stacktec.persistenceapi.service.post.TagService;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/api/post")
@Api(value = "PostRelacional", description = "post api", tags = {"Post"})
@Validated
public class PostControllerRelacional extends BaseController<PostService, Post, PostDto>{
	
    private final PostService postService;
    
    private final RespostaService respostaService;
    
    private final ComentarioService comentarioService;
    
    private final DisciplinaService disciplinaService;
	
	private final UserInternalService userService;	
	
	private final TagService tagService;
		
	public PostControllerRelacional(PostService postService, UserInternalService userService,
				TagService tagService, RespostaService respostaService, ComentarioService comentarioService,
				DisciplinaService disciplinaService) {
		this.postService = postService;
		this.userService = userService;
		this.tagService = tagService;
		this.respostaService = respostaService;
		this.comentarioService = comentarioService;
		this.disciplinaService = disciplinaService;
	}

	
	@ApiOperation(value = "Get post by id")
	@GetMapping("/v1.1/{id}")
	@Transactional(readOnly = true)
	public ResponseEntity getPostById(@PathVariable(value = "id") Long elementId) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Post element = (Post) service.findById(elementId);
		if(element != null) {
			return ResponseEntity.ok(convertToDetailDto(element));
		}		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Upload post")
	@PostMapping("/v1.1/create")
	@Transactional
	public ResponseEntity createElement(@Valid @RequestBody PostDto postDto) {
				
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Post converted = convertToModel(postDto);
		Post elementCreated = (Post) postService.createElement(converted);
		if(elementCreated != null) {						
			ObjectNode response = objectMapper.createObjectNode();								
			response.put("id", ((BaseModel<Long>) (elementCreated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Update post")
	@PutMapping("/v1.1/update/{id}")
	@Transactional
	public ResponseEntity updatePost(@PathVariable(value = "id") Long postId, @Valid @RequestBody PostDto postDto) {
				
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Post converted = postService.updatePost(modelMapper, postDto);
		Post elementUpdated = (Post) postService.updateElement(postId, converted);
		if(elementUpdated != null) {						
			ObjectNode response = objectMapper.createObjectNode();								
			response.put("id", ((BaseModel<Long>) (elementUpdated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
		


	@Override
	protected List<?> convertToListDto(List<Post> postList) {
		if(postList != null && !postList.isEmpty()) {
			return postList.stream().map(this::convertToDetailDto).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}


	@Override
	protected Object convertToDetailDto(Post post) {
		Disciplina disciplina = null;
		UserInternal autor = null;
		
		if(post.getDisciplina() != null) {
			disciplina = post.getDisciplina();
			post.setDisciplina(null);
		}
		if(post.getAutor() != null) {
			autor = post.getAutor();
			post.setAutor(null);
		}
		
		if(post.getTags() != null) {
			Set<Tag> listTags = new HashSet<>();
			for(Tag tag: post.getTags()) {
				listTags.add(tag);
			}
			post.setTags(listTags);
		}
		
		PostDto postDto = modelMapper.map(post, PostDto.class);
		
		if(post.getTags() != null) {
			Set<String> listTagsDto = new HashSet<>();
			for(Tag tag: post.getTags()) {
				listTagsDto.add(tag.getNome());
			}
			postDto.setTags(listTagsDto);
		}
		
		if(disciplina != null) {
			postDto.setDisciplinaId(disciplina.getId());
		}
		
		if(autor != null) {
			postDto.setAutorId(autor.getId());
		}
		
		return postDto;
	}	

	@Override
	@Transactional
	protected Post convertToModel(PostDto dto) {		
				    
		Post post = modelMapper.map(dto, Post.class);		
		Set<Resposta> respostasPost = new HashSet<>(0);
		Set<Tag> tagsPost = new HashSet<>(0);
		Set<Comentario> comentariosPost = new HashSet<>(0);
		
		//Define o atual usuário logado como autor
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
		UserInternal user = userService.findByEmail(name);		
		if(user != null)
			post.setAutor(user);	
		
		
		if(dto.getDisciplinaId() != null) {
			Disciplina disciplina = null;
			disciplina = disciplinaService.getOne(dto.getDisciplinaId());
			if(disciplina != null)
				post.setDisciplina(disciplina);
		}
		
		if(post.getRespostas() != null) {
			respostasPost = post.getRespostas();
		}
		
		if(post.getTags() != null) {
			tagsPost = post.getTags();
		}
		
		if(post.getComentarios() != null) {
			comentariosPost = post.getComentarios();
		}
		
		
		tagsPost = postService.findTagsByName(dto.getTags()); 				
		post.setTags(tagsPost);
		
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
		
		return post;
	}	
}
