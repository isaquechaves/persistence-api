package com.fatec.stacktec.persistenceapi.service.post;

import org.springframework.stereotype.Service;

import com.fatec.stacktec.persistenceapi.model.post.Tag;
import com.fatec.stacktec.persistenceapi.repository.post.TagRepository;
import com.fatec.stacktec.persistenceapi.service.CrudServiceJpaImpl;

import lombok.extern.java.Log;

@Log
@Service
public class TagService extends CrudServiceJpaImpl<TagRepository, Tag>{

}