package com.backend.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.ong.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
	
	// Esse método foi criado para que ele possa ser usado como uma forma de verificar se um uuário ja existe por meio de um email cadastrado
	Usuario findByEmail(String email);

}
