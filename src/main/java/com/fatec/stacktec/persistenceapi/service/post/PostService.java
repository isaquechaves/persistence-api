package com.fatec.stacktec.persistenceapi.service.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.stacktec.persistenceapi.dto.post.PostComentarioDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostMinimalDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostPageDto;
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
    private EntityManager entityManager;
	
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
		List<Resposta> respostasPost = new ArrayList<>();
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
						
		
		List<RespostaDto> respostaDtoList = dto.getRespostas();
		dto.setRespostas(null);
		
		if(respostaDtoList != null) {
			List<Resposta> respostaList = new ArrayList<>();
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
				respostasPost = new ArrayList<>(0);
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

	@Transactional
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
	
	public PostPageDto findPostsByTags(ModelMapper modelMapper, List<String> tags, int page, int pageSize) {
	    String jpql = "SELECT p FROM Post p JOIN p.tags t WHERE t.nome IN :tags " +
	                  "GROUP BY p HAVING COUNT(DISTINCT t) = :tagCount";
	    
	    TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
	    query.setParameter("tags", tags);
	    query.setParameter("tagCount", Long.valueOf(tags.size()));
	    
	    query.setFirstResult((page - 1) * pageSize);
	    query.setMaxResults(pageSize);
	    
	    List<Post> posts = query.getResultList();
	    List<PostMinimalDto> postsMinimalsDtos = modelMapper.map(posts, new TypeToken<List<PostMinimalDto>>() {}.getType());

	    // Get the total count
	    String countJpql = "SELECT COUNT(DISTINCT p) FROM Post p JOIN p.tags t WHERE t.nome IN :tags";
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
	    countQuery.setParameter("tags", tags);
	    Long total = countQuery.getSingleResult();
	    Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalPages = total.intValue();
	    return new PostPageDto(totalResults, totalPages, postsMinimalsDtos);
	}

	public PostPageDto findPostsByTagPaginated(ModelMapper modelMapper, String nome, int page, int pageSize) {
	    String jpql = "SELECT p FROM Post p JOIN p.tags t WHERE t.nome = :nome";
	    
	    TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
	    query.setParameter("nome", nome);
	    
	    query.setFirstResult((page - 1) * pageSize);
	    query.setMaxResults(pageSize);
	    
	    
	    List<Post> posts = query.getResultList();
	    List<PostMinimalDto> postsMinimalsDtos = modelMapper.map(posts, new TypeToken<List<PostMinimalDto>>() {}.getType());

	    // Get the total count
	    String countJpql = "SELECT COUNT(DISTINCT p) FROM Post p JOIN p.tags t WHERE t.nome = :nome";
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
	    countQuery.setParameter("nome", nome);
	    Long total = countQuery.getSingleResult();
	    Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalPages = total.intValue();
	    return new PostPageDto(totalResults, totalPages, postsMinimalsDtos);
	}
	
}
