package com.fatec.stacktec.persistenceapi.controller.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.TypeToken;
import org.springframework.util.ObjectUtils;

import com.fatec.stacktec.persistenceapi.controller.BaseController;
import com.fatec.stacktec.persistenceapi.dto.post.TagDto;
import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Tag;
import com.fatec.stacktec.persistenceapi.service.post.TagService;

public class TagControllerRelacional extends BaseController<TagService, Tag, TagDto>{

	
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
