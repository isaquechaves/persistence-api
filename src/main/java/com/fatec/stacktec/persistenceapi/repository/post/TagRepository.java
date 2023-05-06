package com.fatec.stacktec.persistenceapi.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

}
