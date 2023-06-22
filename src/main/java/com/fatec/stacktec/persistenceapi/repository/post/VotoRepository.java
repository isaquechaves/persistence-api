package com.fatec.stacktec.persistenceapi.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fatec.stacktec.persistenceapi.model.post.Post;
import com.fatec.stacktec.persistenceapi.model.post.Voto;
import com.fatec.stacktec.persistenceapi.model.user.UserInternal;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
	boolean existsByUsuarioAndPost(UserInternal user, Post post);
	
	@Query("SELECT COUNT(v) FROM Voto v WHERE v.post = :post")
    Integer countVotesByPost(@Param("post") Post post);

	@Query("SELECT v FROM Voto v WHERE v.post.id = :postId AND v.usuario.id = :usuarioId")
    Voto findByPostAndUsuarioIds(@Param("postId") Long postId, @Param("usuarioId") Long usuarioId);
}
