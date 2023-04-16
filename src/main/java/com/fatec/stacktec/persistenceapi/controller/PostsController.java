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
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.payload.response.UserInfoResponse;
import com.fatec.stacktec.persistenceapi.repository.user.UserInternalRepository;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

@RestController
@RequestMapping("/api/post")
@Validated
public class PostsController {
	
	@Autowired
    private UserInternalService userServicePost;
	
	@Autowired
    private UserInternalRepository userRepositoryPost;
	
	
	@GetMapping("/user")
	public ResponseEntity<?> getUser(@RequestBody LoginDto loginDto) {
		UserInternal user = userServicePost.findByEmail(loginDto.getEmail());
		
		List<String> roles = user.getRoles().stream()
		        .map(item -> item.getName())
		        .collect(Collectors.toList());
		
		return ResponseEntity.ok().body(new UserInfoResponse(user.getEmail(), roles));
	}
}
