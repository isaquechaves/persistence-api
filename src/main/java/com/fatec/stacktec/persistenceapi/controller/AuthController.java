package com.fatec.stacktec.persistenceapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fatec.stacktec.persistenceapi.dto.LoginDto;
import com.fatec.stacktec.persistenceapi.dto.SignUpDto;
import com.fatec.stacktec.persistenceapi.exception.BusinessException;
import com.fatec.stacktec.persistenceapi.model.user.Role;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.payload.response.MessageResponse;
import com.fatec.stacktec.persistenceapi.payload.response.UserInfoResponse;
import com.fatec.stacktec.persistenceapi.repository.user.RoleRepository;
import com.fatec.stacktec.persistenceapi.repository.user.UserInternalRepository;
import com.fatec.stacktec.persistenceapi.security.jwt.JwtUtils;
import com.fatec.stacktec.persistenceapi.security.services.UserDetailsImpl;
import com.fatec.stacktec.persistenceapi.service.user.RoleService;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Auth", description = "auth api", tags = {"Auth"})
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInternalRepository userRepository;
    
    @Autowired
    private UserInternalService userService;
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @ApiOperation(value = "Authenticate user")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginDto loginDto){    	    
    	
    	Cache cache = cacheManager.getCache("usersCache");
	    String cacheKey = loginDto.getEmail();
	    ValueWrapper valueWrapper = cache.get(cacheKey);	    	    
	    if (valueWrapper != null) {
	        // User is already authenticated, return the cached token

		    String username = jwtUtils.getUserNameFromJwtToken((String)valueWrapper.get());
		    UserInternal userInternal = userService.findByEmail(username);	 
	        String token = (String) valueWrapper.get();
	        List<String> roles = userInternal.getRoles().stream()
	    	        .map(item -> item.getName())
	    	        .collect(Collectors.toList());
	        
	        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, token).body(new UserInfoResponse(userInternal.getEmail(), roles)); 
	    }
	    	        	
    	Authentication authentication = authenticationManager
    	        .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    	
    	if(!loginDto.getEmail().endsWith("@fatec.sp.gov.br")) {
    		throw new BusinessException("Email needs to end with the correct domain");
    	}
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
	    
	    //UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	 
	    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(name);

	    List<String> roles = auth.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());
	    
	    //Store JWT in Cache
	    cache.put(cacheKey, jwtCookie.getValue());

	    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
	        .body(new UserInfoResponse(auth.getName(), roles)); 
	    
	    	    	      
    }

    @ApiOperation(value = "Register user")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpRequest){    	
	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
	    }	    
	    UserInternal user = convertToModel(signUpRequest, null);
	    
	    UserInternal created = userService.createElementAndFlush(user);
	    if(created != null) {
	    	ObjectNode response = objectMapper.createObjectNode();
	    	response.put("id", created.getId());
	    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }else {
	    	return ResponseEntity.noContent().build();
	    }
    }

	private UserInternal convertToModel(SignUpDto dto, UserInternal base) {
		UserInternal user;
		if(base == null) {
			user = new UserInternal(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
		}else {
			user = base;
		}
        user.setApelido(dto.getApelido());
		
		Set<Role> rolesUser = new HashSet<>();
		if(user != null) {
			if(user.getRoles() != null) {
				rolesUser = user.getRoles();
				rolesUser.clear();
			}
		}
		
		if(dto.getRoles() != null && !dto.getRoles().isEmpty()) {
			for(Long roleId : dto.getRoles()) {
				Role role = null;
				if(roleId != null && roleId >0) {
					role = roleService.findById(roleId);
				}
				if(role != null) {
					rolesUser.add(role);
				}
			}
		}
		user.setRoles(rolesUser);
		
		return user;
	}
}