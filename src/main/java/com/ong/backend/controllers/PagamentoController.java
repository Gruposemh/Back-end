package com.ong.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.PagamentoDTO;
import com.ong.backend.entities.Pagamento;
import com.ong.backend.services.PagamentoService;

@RestController
@RequestMapping(value = "/pagamento")
public class PagamentoController {

	@Autowired
	PagamentoService pagamentoService;
	
	@PostMapping(value = "/efetuar")
	public ResponseEntity<?> pagar(@RequestBody PagamentoDTO dto){
		return pagamentoService.pagar(dto);
	}
	
	@GetMapping(value = "/listar")
	public List<Pagamento> pagar(){
		return pagamentoService.listar();
	}
}
