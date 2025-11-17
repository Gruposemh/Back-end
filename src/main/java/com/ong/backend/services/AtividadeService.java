package com.ong.backend.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ong.backend.dto.AtividadeDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Atividade;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.AtividadeRepository;

@Service
public class AtividadeService {

	@Autowired
	AtividadeRepository atividadeRepository;
	
	public ResponseEntity<Atividade> cadastrarAtividade(AtividadeDTO dto){
		Atividade atividade = new Atividade();
		
		atividade.setDescricao(dto.getDescricao());
		atividade.setNome(dto.getNome());
		atividade.setHorario(dto.getHorario());
		atividade.setDias(dto.getDias());
		atividade.setVagas(dto.getVagas());
		atividade.setImagem(dto.getImagem());
		
		atividade = atividadeRepository.save(atividade);
		
		return ResponseEntity.ok(atividade);
	}
	
	public ResponseEntity<List<Atividade>> buscarPorNome(String nomeAtividade) {
        List<Atividade> atividades = atividadeRepository.findAllByNomeContainingIgnoreCase(nomeAtividade);
        if (atividades.isEmpty()) {
            throw new NaoEncontradoException("Nenhuma atividade encontrada com o nome: " + nomeAtividade);
        }
        return ResponseEntity.ok(atividades);
    }	
	
	public List<Atividade> listar(){
		return atividadeRepository.findAll();
	}
	
	public ResponseEntity<MensagemResponse> excluirAtividade(Long id) {
	    atividadeRepository.deleteById(id);
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(new MensagemResponse("Atividade excluída!"));
	}
	
	public ResponseEntity<Atividade> atualizarAtividade(Long id, AtividadeDTO atualizado){
		Optional<Atividade> atividades = atividadeRepository.findById(id);
        if (atividades.isEmpty()) {
            throw new NaoEncontradoException("Nenhuma atividade encontrada com o ID: " + id);
        }
        
        Atividade atividade = atividadeRepository.findById(id).get();
        atividade.setNome(atualizado.getNome());
        atividade.setDescricao(atualizado.getDescricao());
        atividade.setVagas(atualizado.getVagas());
        atividade.setDias(atualizado.getDias());
        atividade.setHorario(atualizado.getHorario());
        atividade.setImagem(atualizado.getImagem());
        atividade = atividadeRepository.save(atividade);
        
        return ResponseEntity.ok(atividade);
	}
}

