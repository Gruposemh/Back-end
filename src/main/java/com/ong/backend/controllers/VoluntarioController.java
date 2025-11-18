package com.ong.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.VoluntarioDTO;
import com.ong.backend.entities.Voluntario;
import com.ong.backend.services.VoluntarioService;

@RestController
@RequestMapping("/voluntario")
public class VoluntarioController {

    @Autowired
    VoluntarioService voluntarioService;

    @PostMapping("/tornar")
    public ResponseEntity<Voluntario> tornarVoluntario(@RequestBody VoluntarioDTO dto) {
        return voluntarioService.tornarVoluntario(dto);
    }

    @GetMapping("/listar")
    public List<Voluntario> listarVoluntarios() {
        return voluntarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voluntario> buscarPorId(@PathVariable Long id) {
        return voluntarioService.buscarPorId(id);
    }

    @GetMapping("/listar/pendentes")
    public List<Voluntario> listarPendentes() {
        return voluntarioService.listarPendentes();
    }

    @GetMapping("/listar/aprovados")
    public List<Voluntario> listarAprovados() {
        return voluntarioService.listarAprovados();
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Long idUsuario) {
        return voluntarioService.buscarPorUsuario(idUsuario);
    }

    
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @RequestBody VoluntarioDTO atualizado){
    	return voluntarioService.editarPerfil(id, atualizado);
    }

    @PutMapping("/aprovar/{id}")
    public ResponseEntity<MensagemResponse> aprovar(@PathVariable Long id) {
        return voluntarioService.aprovar(id);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        return voluntarioService.cancelar(id);
    }
}
