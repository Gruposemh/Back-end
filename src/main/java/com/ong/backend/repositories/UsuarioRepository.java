package com.ong.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
	
	Optional<Usuario> findByNome(String nome);
	Optional<Usuario> findByEmail(String email);
}
