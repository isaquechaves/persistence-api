package com.fatec.stacktec.persistenceapi.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.TypeToken;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fatec.stacktec.persistenceapi.dto.LoginDto;
import com.fatec.stacktec.persistenceapi.dto.UserInternalDto;
import com.fatec.stacktec.persistenceapi.dto.UserInternalDtoMinimal;
import com.fatec.stacktec.persistenceapi.dto.UserInternalPageDto;
import com.fatec.stacktec.persistenceapi.dto.UserInternalToListDto;
import com.fatec.stacktec.persistenceapi.dto.post.request.ParamsToPaginate;
import com.fatec.stacktec.persistenceapi.exception.BusinessException;
import com.fatec.stacktec.persistenceapi.model.user.Role;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;
import com.fatec.stacktec.persistenceapi.model.util.BaseModel;
import com.fatec.stacktec.persistenceapi.payload.response.MessageResponse;
import com.fatec.stacktec.persistenceapi.payload.response.UserInfoResponse;
import com.fatec.stacktec.persistenceapi.repository.user.RoleRepository;
import com.fatec.stacktec.persistenceapi.repository.user.UserInternalRepository;
import com.fatec.stacktec.persistenceapi.security.jwt.JwtUtils;
import com.fatec.stacktec.persistenceapi.service.user.RoleService;
import com.fatec.stacktec.persistenceapi.service.user.UserInternalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@RestController
@CrossOrigin
@Api(value = "Auth", description = "auth api", tags = {"Auth"})
@RequestMapping("/auth")
@Log
public class AuthController extends BaseController<UserInternalService, UserInternal, UserInternalDto>{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInternalRepository userRepository;
    
    @Autowired
    private UserInternalService userService;
    
    @Autowired
    private RoleService roleService;

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
	    	// Find the index of '=' and ';'
	    	String inputToken = (String) valueWrapper.get();
	    	int startIndex = inputToken.indexOf('=') + 1;
	    	int endIndex = inputToken.indexOf(';');
	    	// Extract the token
	    	String jwtTokenSplit = inputToken.substring(startIndex, endIndex);
	        // User is already authenticated, return the cached token
		    String username = jwtUtils.getUserNameFromJwtToken(jwtTokenSplit);
		    UserInternal userInternal = userService.findByEmail(username);	 
	        String token = (String) valueWrapper.get();
	        List<String> roles = userInternal.getRoles().stream()
	    	        .map(item -> item.getName())
	    	        .collect(Collectors.toList());
	        
	        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, token).body(new UserInfoResponse(userInternal.getName(), userInternal.getEmail(), roles, userInternal.getId(), (String)valueWrapper.get())); 
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
	    cache.put(cacheKey, jwtCookie.toString());
	    UserInternal userEntity = service.findByEmail(name);
	    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
	        .body(new UserInfoResponse(userEntity.getName(), auth.getName(), roles, userEntity.getId(), jwtCookie.toString())); 
	    
	    	    	      
    }

    @ApiOperation(value = "Register user")
    @PostMapping("/signup")
    @Override
    public ResponseEntity createElement(Principal principal, @RequestBody UserInternalDto signUp){ 
    	UserInternalDtoMinimal signUpRequest = modelMapper.map(signUp, UserInternalDtoMinimal.class);
	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
	    }	    
	    UserInternal user = convertToModelCreate(signUpRequest, null);
	    
	    UserInternal created = userService.createElementAndFlush(user);
	    
	    if(created != null) {
		    Authentication authentication = authenticationManager
	    	        .authenticate(new UsernamePasswordAuthenticationToken(signUp.getEmail(), signUp.getPassword()));
		    
		    SecurityContextHolder.getContext().setAuthentication(authentication);	   
		    
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName();
		    
		 
		    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(name);
	
		    List<String> roles = auth.getAuthorities().stream()
		        .map(item -> item.getAuthority())
		        .collect(Collectors.toList());
		    
			Cache cache = cacheManager.getCache("usersCache");
		    String cacheKey = signUp.getEmail();
		    //Store JWT in Cache
		    cache.put(cacheKey, jwtCookie.getValue());
		    UserInternal userEntity = service.findByEmail(name);
		    return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
		        .body(new UserInfoResponse(signUp.getName(), auth.getName(), roles, userEntity.getId(), jwtCookie.toString())); 	    	    
	    }else {
	    	return ResponseEntity.noContent().build();
	    }
    }
    
    @ApiOperation(value = "Update user")
    @PutMapping("/v1.1/{id}")
	@Transactional
    public ResponseEntity updateElement(@PathVariable(value = "id") Long id,
										@Valid @RequestBody UserInternalDto userInternalDto) {    		    	    
	   
	    Authentication authAluno = SecurityContextHolder.getContext().getAuthentication();
	    String nameAluno = authAluno.getName();
		UserInternal usuarioLogadoAluno = service.findByEmail(nameAluno);
	    if (usuarioLogadoAluno != null) { 	    
	        if(usuarioLogadoAluno.getEmail().equals(userInternalDto.getEmail()) ){	        	
	        	UserInternal elementUpdated = service.updateUsuario(modelMapper, id, userInternalDto);
	    		if(elementUpdated != null) {
	    			ObjectNode response = objectMapper.createObjectNode();
	    			response.put("id", ((BaseModel<Long>) (elementUpdated)).getId());
	    			return ResponseEntity.status(HttpStatus.OK).body(response);
	    		}
	        }else{	  	     	    
	    	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    	    String name = auth.getName();
	    		UserInternal usuarioLogado = service.findByEmail(name);
			    List<String> roles = usuarioLogado.getRoles().stream().map(Role::getName).collect(Collectors.toList());		    
		        if(roles.contains("ROLE_ADMIN")){
		        	UserInternal elementUpdated = service.updateUsuarioByAdmin(modelMapper, id, userInternalDto);
		    		if(elementUpdated != null) {
		    			ObjectNode response = objectMapper.createObjectNode();
		    			response.put("id", ((BaseModel<Long>) (elementUpdated)).getId());
		    			return ResponseEntity.status(HttpStatus.OK).body(response);
		    		}	        	        	
		        }else {
		        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		        }
	        }
        }	    
    	return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    @GetMapping("/v1.1/all/{pageNumber}/{pageSize}")
  	@Transactional
  	@Validated
  	public ResponseEntity getAllUsersPaginated(@PathVariable Integer pageNumber, @PathVariable  Integer pageSize) {
      	
    	if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
    	
      	UserInternalPageDto paginatedUsers = service.findAllUsersPaginated(modelMapper, pageNumber, pageSize);
      	
      	return ResponseEntity.ok(paginatedUsers);
  }
    
    @GetMapping("/v1.1/{pageNumber}/{pageSize}/{email}")
  	@Transactional
  	@Validated
  	public ResponseEntity getUsersPaginated(@PathVariable Integer pageNumber, @PathVariable  Integer pageSize, @PathVariable String email) {
      	
    	if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
    	
      	UserInternalPageDto paginatedUsers = service.findUsersByEmailPaginated(modelMapper, pageNumber, pageSize, email);
      	
      	return ResponseEntity.ok(paginatedUsers);
    }
    
	private UserInternal convertToModelCreate(UserInternalDtoMinimal dto, UserInternal base) {
		UserInternal user = new UserInternal(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
		
        user.setApelido(dto.getApelido());
		
		Set<Role> rolesUser = new HashSet<>();
		rolesUser.add(roleService.findByName("ROLE_ALUNO"));
		user.setRoles(rolesUser);
			
		user.setRoles(rolesUser);
		user.setName(dto.getName());
		user.setSemestre(dto.getSemestre());
		
		return user;
	}
	
	private UserInternal convertToModel(UserInternalDto dto, UserInternal base) {
		UserInternal user;
		if(base != null &&  !ObjectUtils.isEmpty(base)) {
			user = base;
		}else { 
			user = service.findByEmail(dto.getEmail());
		}
		
        user.setApelido(dto.getApelido());
		
		Set<Role> rolesUser = new HashSet<>();
		for(String roleName : dto.getRoles()) {
			rolesUser.add(roleService.findByName(roleName));
		}			
		user.setRoles(rolesUser);
		user.setName(dto.getName());
		user.setSemestre(dto.getSemestre());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		return user;		
	}

	@Override
	protected List<?> convertToListDto(List<UserInternal> elements) {
		return modelMapper.map(elements,  new TypeToken<List<UserInternalToListDto>>() {}.getType());
	}

	@Override
	protected Object convertToDetailDto(UserInternal element) {
		UserInternalToListDto userInternalDto = modelMapper.map(element, UserInternalToListDto.class);
		return userInternalDto;
	}

	@Override
	protected UserInternal convertToModel(UserInternalDto dto) {
		return convertToModel(dto, null);
	}
}