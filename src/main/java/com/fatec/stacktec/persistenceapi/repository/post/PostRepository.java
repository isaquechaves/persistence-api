package com.fatec.stacktec.persistenceapi.repository.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Optional<Post> getById(@Param("id") Long id);

}
