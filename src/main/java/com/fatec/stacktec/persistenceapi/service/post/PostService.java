package com.fatec.stacktec.persistenceapi.service.post;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.repository.post.PostRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class PostService extends CrudServiceJpaImpl<PostRepository, Post>{
	
	

}
