package com.fatec.stacktec.persistenceapi.controller.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.stacktec.persistenceapi.controller.BaseController;
import com.fatec.stacktec.persistenceapi.dto.post.TagDto;
import com.fatec.stacktec.persistenceapi.dto.tag.TagPageDto;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Tag;
import com.fatec.stacktec.persistenceapi.service.post.TagService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@Api(value = "Tag", description = "tag api", tags = {"Tag"})
@RequestMapping("/api/tag")
@Validated
public class TagControllerRelacional extends BaseController<TagService, Tag, TagDto>{

	
	@ApiOperation(value = "Get all tags paginated and order by qtdePost DESC")
	@GetMapping("/v1.1/paginated-desc/{pageNumber}/{pageSize}")
	@Transactional
	@CrossOrigin
	public ResponseEntity<?> getAllTagsPaginatedSortDesc(@PathVariable Integer pageNumber, @PathVariable  Integer pageSize){
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		TagPageDto paginatedTags = service.findTagsPaginated(modelMapper, pageNumber, pageSize);
		if(paginatedTags != null && !ObjectUtils.isEmpty(paginatedTags)) {
			return ResponseEntity.ok(paginatedTags);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Get tag by name")
	@GetMapping("/v1.1/name/{name}")
	@Transactional
	@CrossOrigin
	public ResponseEntity<?> getTagByName(@PathVariable String name, HttpSession session){
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		List<TagDto> tagDtos = service.findTagByName(modelMapper, name);
	    if (tagDtos != null && !ObjectUtils.isEmpty(tagDtos)) {
			return ResponseEntity.ok(tagDtos);
	    }
		
		return ResponseEntity.noContent().build();
	}
	
	@Override
	protected List<?> convertToListDto(List<Tag> tagList) {
		return modelMapper.map(tagList, new TypeToken<List<TagDto>>() {}.getType());
	}

	@Override
	protected Object convertToDetailDto(Tag tag) {
		TagDto tagDto = modelMapper.map(tag, TagDto.class);
		return tagDto;
	}

	@Override
	protected Tag convertToModel(TagDto dto) {
		return convertToModel(dto, null);
	}

	private Tag convertToModel(TagDto dto, Tag base) {
		Tag tag;
		if(base != null && !ObjectUtils.isEmpty(base)) {
			tag = base;
		}else {			
			tag = new Tag(dto.getNome(), 0, null);
		}
		Set<Post> postsTag = new HashSet<>(0);
		tag.setPost(postsTag);
		return tag;
	}

}
