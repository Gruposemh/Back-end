package com.ong.backend.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.VoluntarioDTO;
import com.ong.backend.entities.Usuario;
import com.ong.backend.entities.Voluntario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.repositories.VoluntarioRepository;

@Service
public class VoluntarioService {
	
	@Autowired
	VoluntarioRepository voluntarioRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public ResponseEntity<Voluntario> tornarVoluntario(@RequestBody VoluntarioDTO dto) {
	    Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
	            .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate nascimento = LocalDate.parse(dto.getDataNascimento(), formatter);

	    int idade = Period.between(nascimento, LocalDate.now()).getYears();

	    if (idade < 18) {
	        throw new IllegalArgumentException("Usuário precisa ter mais de 18 anos para se tornar voluntário.");
	    }

	    Voluntario voluntario = new Voluntario();
	    voluntario.setCpf(dto.getCpf());
	    voluntario.setDataVoluntario(LocalDateTime.now());
	    voluntario.setIdUsuario(usuario);
	    voluntario.setDataNascimento(dto.getDataNascimento());
	    voluntario.setTelefone(dto.getTelefone());
	    voluntario.setDescricao(dto.getDescricao());
	    voluntario.setEndereco(dto.getEndereco());
	    
	    voluntario = voluntarioRepository.save(voluntario);

	    return ResponseEntity.ok(voluntario);
	}
	
	public List<Voluntario> listar(){
		return voluntarioRepository.findAll();
	}
	
	public ResponseEntity<MensagemResponse> cancelar (@PathVariable Long id){
		Voluntario voluntario = voluntarioRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Voluntário não encontrado"));
		voluntarioRepository.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Solicitação cancelada!"));
	}
}
