package com.ong.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository <Blog, Long>{

	Optional<Blog> findByTituloMateria(String tituloMateria);
}
