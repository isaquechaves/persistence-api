package com.fatec.stacktec.persistenceapi.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fatec.stacktec.persistenceapi.model.util.BaseModel;
import com.fatec.stacktec.persistenceapi.service.CrudService;

import io.swagger.annotations.ApiOperation;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class BaseController<K extends CrudService, MODEL, DTO> {
	
	@Autowired
	protected ModelMapper modelMapper;
	
	@Autowired 
	protected ObjectMapper objectMapper;
	
	@Autowired
	protected K service;
	
	
	@ApiOperation("Get all elements")
	@GetMapping("/v1.0/all")
	@Transactional(readOnly = true)
	public ResponseEntity<?> getAllElements(Principal principal) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<MODEL> elements = (List<MODEL>) service.findAll();
		if(!ObjectUtils.isEmpty(elements)) {
			return ResponseEntity.ok(convertToListDto(elements));
		}
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Get all elements")
	@GetMapping("/v1.0/all/pagination")
	@Transactional(readOnly = true)
	public ResponseEntity getAllElements(Principal principal, Pageable pageable) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Page<MODEL> pages = (Page<MODEL>) service.findAll(pageable);
		if(pages != null && pages.getTotalElements() > 0) {
			return ResponseEntity.ok(new PageImpl(convertToListDto(pages.getContent()), pageable, pages.getTotalElements()));
		}
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Get element by id")
	@GetMapping("/v1.0/{id}")
	@Transactional(readOnly = true)
	public ResponseEntity getElementById(Principal principal, @PathVariable(value = "id") Long elementId) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		MODEL element = (MODEL) service.findById(elementId);
		if(element != null) {
			return ResponseEntity.ok(convertToDetailDto(element));
		}		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Create a new element")
	@PostMapping("/v1.0")
	@Transactional
	public ResponseEntity createElement(Principal principal, @Valid @RequestBody DTO element) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		MODEL converted = convertToModel(element);
		MODEL elementCreated = (MODEL) service.createElement((BaseModel) converted);
		if(elementCreated != null) {
			ObjectNode response = objectMapper.createObjectNode();
			response.put("id", ((BaseModel<Long>) (elementCreated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Update a element")
	@PutMapping("/v1.0/{id}")
	@Transactional
	public ResponseEntity updateElement(Principal principal, 
										@PathVariable(value = "id") Long elementId,
										@Valid @RequestBody DTO element) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		MODEL converted = convertToModel(element);
		MODEL elementUpdated = (MODEL) service.updateElement(elementId, (BaseModel) converted);
		if(elementUpdated != null) {
			ObjectNode response = objectMapper.createObjectNode();
			response.put("id", ((BaseModel<Long>) (elementUpdated)).getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Delete a element")
	@DeleteMapping("/v1.0/{id}")
	public ResponseEntity deleteElement(Principal principal,
										@PathVariable(value = "id") Long elementId) {
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		boolean success = service.deleteElement(elementId);
		if(success) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		return ResponseEntity.noContent().build();
	}
	
	protected abstract List<?> convertToListDto(List<MODEL> elements);
	
	protected abstract Object convertToDetailDto(MODEL element);
	
	protected abstract MODEL convertToModel(DTO dto);

}
