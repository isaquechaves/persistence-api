package com.fatec.stacktec.persistenceapi.service.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.dto.post.TagDto;
import com.fatec.stacktec.persistenceapi.dto.tag.TagPageDto;
import com.fatec.stacktec.persistenceapi.model.post.Tag;
import com.fatec.stacktec.persistenceapi.repository.post.TagRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class TagService extends CrudServiceJpaImpl<TagRepository, Tag>{
	
	@Autowired
	private EntityManager entityManager;
	
	
	public Optional<Tag> getByNome(String nome) {
		return repository.getByNome(nome);
	}

	public TagPageDto findTagsPaginated(ModelMapper modelMapper, Integer pageNumber, Integer pageSize) {
		String jpql = "SELECT t from Tag t ORDER BY t.qtdePosts DESC";
		
		TypedQuery<Tag> query = entityManager.createQuery(jpql, Tag.class);
		
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		
		List<Tag> tags = query.getResultList();
		List<TagDto> tagDtos = modelMapper.map(tags, new TypeToken<List<TagDto>>() {}.getType());
		
		String countJpql = "SELECT COUNT(t) from Tag t";
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
	    Long total = countQuery.getSingleResult();
	    Integer totalResults = Double.valueOf(Math.ceil(total / (double) pageSize)).intValue();
	    Integer totalPages = total.intValue();
	    return new TagPageDto(totalPages, totalResults, tagDtos);
	}
	
	public List<TagDto> findTagByName(ModelMapper modelMapper, String name) {
		String jpql = "SELECT t from Tag t WHERE LOWER(t.nome) LIKE LOWER(:name) ORDER BY t.nome ASC";
		
		TypedQuery<Tag> query = entityManager.createQuery(jpql, Tag.class);
		query.setParameter("name", "%" + name + "%");
		
		List<Tag> tags = query.getResultList();
		List<TagDto> tagDtos = modelMapper.map(tags, new TypeToken<List<TagDto>>() {}.getType());

	    return tagDtos;
	}

}
