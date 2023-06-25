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
import com.fatec.stacktec.persistenceapi.model.post.Voto;
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
	
	@Autowired
	private VotoService votoService;
	
	@Transactional
	public Post updatePost(ModelMapper modelMapper, PostDto dto) {
		Post post = modelMapper.map(dto, Post.class);		
		List<Resposta> respostasPost = new ArrayList<>();
		Set<Tag> tagsPost = new HashSet<>(0);
		List<Comentario> comentariosPost = new ArrayList<>();		
		
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
		
		
		List<PostComentarioDto> comentarioDtoList = dto.getComentarios();
		dto.setComentarios(null);
		
		if(comentarioDtoList != null) {
			List<Comentario> comentarioList = new ArrayList<>();
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
				comentariosPost = new ArrayList<>();
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
	    String jpql = "SELECT p FROM Post p JOIN p.tags t WHERE LOWER(t.nome) IN :tags " +
	                  "GROUP BY p HAVING COUNT(DISTINCT t) = :tagCount";
	    
	    List<String> lowercaseTags = tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
	    
	    TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
	    query.setParameter("tags", lowercaseTags);
	    query.setParameter("tagCount", Long.valueOf(tags.size()));
	    
	    query.setFirstResult((page - 1) * pageSize);
	    query.setMaxResults(pageSize);
	    
	    List<Post> posts = query.getResultList();
	    List<PostMinimalDto> postsMinimalsDtos = new ArrayList<>();
		for(Post post : posts) {
			List<String> tagsDto = post.getTags().stream().map(Tag::getNome).collect(Collectors.toList());
			post.setTags(null);
			List<Voto> votos = post.getVotos();  // Get the votos collection
		    post.setVotos(null); 
			Integer votosCount = 0;
			 if (votos != null && !votos.isEmpty())
			        votosCount = votos.size();
			PostMinimalDto dto = modelMapper.map(post, PostMinimalDto.class);
			dto.setVotos(votosCount);
			dto.setTags(tags);
			postsMinimalsDtos.add(dto);
		}
		
		entityManager.clear();
	    // Get the total count

	    Long total = (long) postsMinimalsDtos.size();
	    Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalPages = total.intValue();
	    return new PostPageDto(totalResults, totalPages, postsMinimalsDtos);
	}

	public PostPageDto findPostsByTagPaginated(ModelMapper modelMapper, String nome, int page, int pageSize) {
	    String jpql = "SELECT p FROM Post p JOIN p.tags t WHERE LOWER(t.nome) = LOWER(:nome)";
	    
	    TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
	    query.setParameter("nome", nome);
	    
	    query.setFirstResult((page - 1) * pageSize);
	    query.setMaxResults(pageSize);
	   
	    List<Post> posts = query.getResultList();
	    List<PostMinimalDto> postsMinimalsDtos = new ArrayList<>();
		for(Post post : posts) {
			List<String> tags = post.getTags().stream().map(Tag::getNome).collect(Collectors.toList());
			post.setTags(null);
			List<Voto> votos = post.getVotos();  // Get the votos collection
		    post.setVotos(null); 
		    Integer votosCount = 0;
		    List<Resposta> respostas = post.getRespostas();  // Get the respostas collection
		    post.setRespostas(null);			
			Integer respostaCount = 0;
			if (votos != null && !votos.isEmpty())				
		        votosCount = votos.size();
			if (respostas != null && !respostas.isEmpty())				
				respostaCount = respostas.size();
			PostMinimalDto dto = modelMapper.map(post, PostMinimalDto.class);
			dto.setRespostas(respostaCount);
			dto.setVotos(votosCount);
			dto.setTags(tags);
			postsMinimalsDtos.add(dto);
		}
		
		entityManager.clear();
	    // Get the total count
	    String countJpql = "SELECT COUNT(DISTINCT p) FROM Post p JOIN p.tags t WHERE LOWER(t.nome) = LOWER(:nome)";
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
	    countQuery.setParameter("nome", nome);
	    Long total = countQuery.getSingleResult();
	    Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalPages = total.intValue();
	    return new PostPageDto(totalResults, totalPages, postsMinimalsDtos);
	}
	
	@Transactional
	public boolean deletePost(Long elementId) {
		Post postToDelete = getOne(elementId);		
		for(Tag tag : postToDelete.getTags()) {		
			if(tag.getQtdePosts() > 0) {
				tag.setQtdePosts(tag.getQtdePosts()-1);
			}
			tagService.updateElement(tag.getId(), tag);
		}
		boolean success = this.deleteElement(elementId);
		return success;
	}

	public List<PostMinimalDto> getFirstTenPosts(ModelMapper modelMapper) {
		List<Post> postList = repository.findTop10ByOrderByCreatedAtDesc();
		List<PostMinimalDto> postsMinimalsDtos = new ArrayList<>();
		for(Post post : postList) {
			List<String> tags = post.getTags().stream().map(Tag::getNome).collect(Collectors.toList());
			post.setTags(null);
			List<Voto> votosCopy = new ArrayList<>(post.getVotos());  // Create a copy of the votos collection
	        post.setVotos(null);
	        Integer votosCount = 0;
	        List<Resposta> respostas = post.getRespostas();  // Get the respostas collection
		    post.setRespostas(null);	
			Integer respostaCount = 0;
		    if (respostas != null && !respostas.isEmpty())	
		    	respostaCount = respostas.size();
	        if (votosCopy != null && !votosCopy.isEmpty())
	            votosCount = votosCopy.size();
			PostMinimalDto dto = modelMapper.map(post, PostMinimalDto.class);
			dto.setRespostas(respostaCount);
			dto.setVotos(votosCount);
			dto.setTags(tags);
			postsMinimalsDtos.add(dto);
		}
		entityManager.clear();
		return postsMinimalsDtos;
	}

	public PostPageDto findPostsPageable(ModelMapper modelMapper, String order, Integer pageNumber, Integer pageSize) {
		String jpql = null;
		if(order.equals("recentes")) {
			jpql = "SELECT p FROM Post p  ORDER BY p.criadoEm DESC";
		}else if(order.equals("antigos")) {
			jpql = "SELECT p from Post p ORDER by p.criadoEm ASC";
		}else if (order.equals("maisvotados")) {
			jpql = "SELECT p FROM Post p LEFT JOIN p.votos v GROUP BY p ORDER BY COUNT(v) DESC";
		}
	   
		TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
	   
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<Post> posts = query.getResultList();
		
		List<PostMinimalDto> postsMinimalsDtos = new ArrayList<>();
		for(Post post : posts) {
			List<String> tags = post.getTags().stream().map(Tag::getNome).collect(Collectors.toList());
			post.setTags(null);
			List<Voto> votos = post.getVotos();  // Get the votos collection
		    post.setVotos(null); 
			Integer votosCount = 0;
			List<Resposta> respostas = post.getRespostas();  // Get the respostas collection
		    post.setRespostas(null);	
			Integer respostaCount = 0;
		    if (respostas != null && !respostas.isEmpty())	
	    		respostaCount = respostas.size();
		    if (votos != null && !votos.isEmpty())
		        votosCount = votos.size();
			PostMinimalDto dto = modelMapper.map(post, PostMinimalDto.class);
			dto.setRespostas(respostaCount);
			dto.setVotos(votosCount);
			dto.setTags(tags);
			postsMinimalsDtos.add(dto);
		}
		
		entityManager.clear();
		
		// Get the total count
		String countJpql = "SELECT COUNT(DISTINCT p) FROM Post p";
		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		Long total = countQuery.getSingleResult();
		Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
		Integer totalPages = total.intValue();
		return new PostPageDto(totalResults, totalPages, postsMinimalsDtos);
	}
	

	public PostPageDto searchPostsByTitle(String searchString, ModelMapper modelMapper) {	
		Integer pageSize = 10;
	    String[] searchWords = searchString.split(" ");

	    // Create the JPQL query
	    StringBuilder jpqlQuery = new StringBuilder("SELECT p FROM Post p WHERE ");

	    // Add the LIKE conditions for each word in the search string
	    for (int i = 0; i < searchWords.length; i++) {
	        jpqlQuery.append("p.titulo LIKE :searchWord").append(i);
	        if (i < searchWords.length - 1) {
	            jpqlQuery.append(" or ");
	        }
	    }

	    // Execute the query
	    TypedQuery<Post> query = entityManager.createQuery(jpqlQuery.toString(), Post.class);

	    // Set the search word parameters
	    for (int i = 0; i < searchWords.length; i++) {
	        query.setParameter("searchWord" + i, "%" + searchWords[i] + "%");
	    }

	    List<Post> posts = query.getResultList();
	    
	    
	    List<PostMinimalDto> postsMinimalsDtos = new ArrayList<>();
		for(Post post : posts) {
			List<String> tags = post.getTags().stream().map(Tag::getNome).collect(Collectors.toList());
			post.setTags(null);
			List<Voto> votos = post.getVotos();  // Get the votos collection
		    post.setVotos(null); 
			Integer votosCount = 0;
			List<Resposta> respostas = post.getRespostas();  // Get the respostas collection
		    post.setRespostas(null);	
			Integer respostaCount = 0;
		    if (respostas != null && !respostas.isEmpty())	
		    	respostaCount = respostas.size();
		    if (votos != null && !votos.isEmpty())
		        votosCount = votos.size();
			PostMinimalDto dto = modelMapper.map(post, PostMinimalDto.class);
			dto.setRespostas(respostaCount);
			dto.setVotos(votosCount);
			dto.setTags(tags);
			postsMinimalsDtos.add(dto);
		}
		
		entityManager.clear();

		Long total = (long) postsMinimalsDtos.size();
	    Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalPages = total.intValue();
		
		return new PostPageDto(totalResults, totalPages, postsMinimalsDtos);
	}
}
