package com.ong.backend.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ong.backend.entities.Blog;
import com.ong.backend.entities.StatusPublicacao;

@Repository
public interface BlogRepository extends JpaRepository <Blog, Long>{

	Optional<Blog> findByTituloMateria(String tituloMateria);
	
	List<Blog> findByStatus(StatusPublicacao status);

}
