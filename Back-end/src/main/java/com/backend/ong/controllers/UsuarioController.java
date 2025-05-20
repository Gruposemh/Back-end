package com.backend.ong.controllers;

import java.util.List;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.ong.dto.UsuarioDTO;
import com.backend.ong.entity.Usuario;
import com.backend.ong.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/usuario")
@Tag(name = "Usuário", description = "Operações de usuários")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Operation(summary = "Cadastrar um novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso")
    @PostMapping(value = "/criar")
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO dto) {
    	dto = usuarioService.cadastrarUsuario(dto);
		return ResponseEntity.ok(dto);
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity<?> logar(@RequestBody UsuarioDTO dto) {
        return usuarioService.login(dto);
    }


    @Operation(summary = "Listar todos os usuários", description = "Recupera todos os usuários cadastrados no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuários")
    @GetMapping(value ="/todos")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "Buscar usuário por ID", description = "Recupera um usuário com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> buscarId(@Parameter(description = "ID do usuário a ser recuperado") @PathVariable Long id) {
        return usuarioService.buscarId(id);
    }

    @Operation(summary = "Deletar um usuário", description = "Deleta um usuário com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
    	usuarioService.deleteUsuario(id);
        return ResponseEntity.ok("Usuário deletado");
    }

    @Operation(summary = "Atualizar dados de um usuário", description = "Atualiza as informações de um usuário com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario atualizado) {
        Usuario usuario = usuarioService.atualizarUsuario(id, atualizado);
        return ResponseEntity.ok(usuario);
    }
}
