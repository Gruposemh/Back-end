package com.ong.backend.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ong.backend.dto.CursoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Curso;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.CursoRepository;

@Service
public class CursoService {

	@Autowired
	CursoRepository cursoRepository;
	
	public ResponseEntity<Curso> cadastrarCurso(CursoDTO dto){
		Curso curso = new Curso();
		
		curso.setDescricao(dto.getDescricao());
		curso.setTitulo(dto.getTitulo());
		curso.setValor(dto.getValor());
		
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
	
	public List<Curso> listar(){
		return cursoRepository.findAll();
	}
	
	public ResponseEntity<MensagemResponse> excluirCurso(Long id) {
	    cursoRepository.deleteById(id);
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(new MensagemResponse("Curso excluído!"));
	}
	
	public ResponseEntity<Curso> atualizarCurso(Long id, CursoDTO atualizado){
		Optional<Curso> cursos = cursoRepository.findById(id);
        if (cursos.isEmpty()) {
            throw new NaoEncontradoException("Nenhum curso encontrado com o ID: " + id);
        }
        
        Curso curso = cursoRepository.findById(id).get();
        curso.setValor(atualizado.getValor());
        curso = cursoRepository.save(curso);
        
        return ResponseEntity.ok(curso);
	}
}
