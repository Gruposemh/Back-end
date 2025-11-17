package com.ong.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.CursoDTO;
import com.ong.backend.dto.InscricaoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Curso;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.CursoRepository;
import com.ong.backend.repositories.InscricaoRepository;

@Service
public class CursoService {

	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	InscricaoRepository inscricaoRepository;
	
	public ResponseEntity<Curso> cadastrarCurso(CursoDTO dto){
		Curso curso = new Curso();
		
		curso.setDescricao(dto.getDescricao());
		curso.setTitulo(dto.getTitulo());
		curso.setHorario(dto.getHorario());
		curso.setDias(dto.getDias());
		curso.setVagas(dto.getVagas());
		
		curso = cursoRepository.save(curso);
		
		return ResponseEntity.ok(curso);
	}
	
	public ResponseEntity<List<Curso>> buscarPorTitulo(String tituloCurso) {
        List<Curso> cursos = cursoRepository.findAllByTituloContainingIgnoreCase(tituloCurso);
        if (cursos.isEmpty()) {
            throw new NaoEncontradoException("Nenhum curso encontrado com o título: " + tituloCurso);
        }
        return ResponseEntity.ok(cursos);
    }	
	
	public List<CursoDTO> listar(){
		List<Curso> cursos = cursoRepository.findAll();
	    return cursos.stream()
	        .map(c -> new CursoDTO(
	            c.getId(),
	            c.getTitulo(),
	            c.getDescricao(),
	            c.getDias(),
	            c.getHorario(),
	            c.getVagas()
	        ))
	        .toList();
	}
	
	public CursoDTO listarInscricoesCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        List<Inscricao> inscricoes = inscricaoRepository.findByIdCursoId(cursoId);

        List<InscricaoDTO> inscricaoDTOs = inscricoes.stream()
                .map(inscricao -> new InscricaoDTO(inscricao))
                .collect(Collectors.toList());

        CursoDTO cursoDTO = new CursoDTO(curso);
        cursoDTO.setInscricoes(inscricaoDTOs);

        return cursoDTO;
    }
	
	public ResponseEntity<MensagemResponse> excluirCurso(Long id) {
	    cursoRepository.deleteById(id);
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(new MensagemResponse("Curso excluído!"));
	}
	
	public ResponseEntity<?> atualizarCurso(Long id, CursoDTO atualizado){
		Optional<Curso> cursos = cursoRepository.findById(id);
        if (cursos.isEmpty()) {
            throw new NaoEncontradoException("Nenhum curso encontrado com o ID: " + id);
        }
        
        Curso curso = cursoRepository.findById(id).get();
        curso.setTitulo(atualizado.getTitulo());
        curso.setDescricao(atualizado.getDescricao());
        curso.setVagas(atualizado.getVagas());
        curso.setDias(atualizado.getDias());
        curso.setHorario(atualizado.getHorario());
        curso = cursoRepository.save(curso);
        
        return ResponseEntity.ok().body("Atividade atualizada");
	}
}
