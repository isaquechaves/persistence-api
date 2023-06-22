package com.fatec.stacktec.persistenceapi.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Voto;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.repository.post.VotoRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import lombok.extern.java.Log;

@Log
@Service
public class VotoService extends CrudServiceJpaImpl<VotoRepository, Voto>{	
	
	@Autowired
	private UserInternalService userService;
	
	@Autowired
	private PostService postService;
	
	
	@Transactional
	public boolean userHasVotedInPost(Long userId, Long postId) {
		Post post = null;
		post = postService.getOne(postId);
		UserInternal user = userService.findById(userId);
		
		return repository.existsByUsuarioAndPost(user, post);
	}
	
	public Integer  countVotesByPost(Long postId) {
		Post post = postService.findById(postId);
	    Integer voteCount = repository.countVotesByPost(post);
	    return voteCount == 0 ? null : voteCount;

	}	

	public boolean removeVoto(Long usuarioId, Long postId) {
		Voto voto = repository.findByPostAndUsuarioIds(postId, usuarioId);
		boolean succes = this.deleteElement(voto);
		return succes;
	}	
}
