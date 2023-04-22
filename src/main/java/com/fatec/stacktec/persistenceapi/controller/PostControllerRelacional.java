package com.fatec.stacktec.persistenceapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.stacktec.persistenceapi.dto.LoginDto;
import com.fatec.stacktec.persistenceapi.dto.post.PostDto;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.payload.response.UserInfoResponse;
import com.fatec.stacktec.persistenceapi.repository.post.PostRepository;
import com.fatec.stacktec.persistenceapi.repository.user.UserInternalRepository;
import com.fatec.stacktec.persistenceapi.service.post.PostService;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/post")
@Api(value = "PostRelacional", description = "post api", tags = {"Post"})
@Validated
public class PostControllerRelacional extends BaseController<PostService, Post, PostDto>{
	
    private final PostService postService;
	
	
	public PostControllerRelacional(PostService postService){
		this.postService = postService;
	}


	@Override
	protected List<?> convertToListDto(List<Post> elements) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Object convertToDetailDto(Post element) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Post convertToModel(PostDto dto) {
		// TODO Auto-generated method stub
		return null;
	}
		
}
