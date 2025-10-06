package com.ong.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.TarefasDTO;
import com.ong.backend.entities.Tarefas;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.TarefasRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class TarefasService {

	@Autowired
	private TarefasRepository tarefasRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public ResponseEntity<Tarefas> cadastrarTarefa(TarefasDTO dto) {
		Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
		        .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		Tarefas tarefa = new Tarefas();
		tarefa.setDiaTarefa(dto.getDataTarefa());
		tarefa.setNomeTarefa(dto.getNomeTarefa());
		tarefa.setUsuario(usuario);
		
		tarefa = tarefasRepository.save(tarefa);
		
		return ResponseEntity.ok(tarefa);
	}
	
	public List<Tarefas> listarTarefas() {
		return tarefasRepository.findAll();
	}
	
	public List<Tarefas> tarefasUsuario(Long idUsuario){
		return tarefasRepository.findByUsuarioId(idUsuario);
	}
	
	public ResponseEntity<MensagemResponse> deletarTarefa(Long id) {
		tarefasRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Tarefa excluida!"));
	}
	
	public ResponseEntity<Tarefas> atualizarTarefa(Long id, TarefasDTO atualizado) {
	    Tarefas tarefa = tarefasRepository.findById(id).orElseThrow();

	    tarefa.setNomeTarefa(atualizado.getNomeTarefa());
	    tarefa.setDiaTarefa(atualizado.getDataTarefa());
	    
	    tarefa = tarefasRepository.save(tarefa);
	    return ResponseEntity.ok(tarefa);
	}
}
