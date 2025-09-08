package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.services.RelatorioService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    RelatorioService relatorio;

    @GetMapping(value = "/usuarios")
    public ResponseEntity<String> relatorioUsuarios(@RequestParam String caminho) throws JRException {
        relatorio.gerarRelatorioUsuariosPDF(caminho);
        return ResponseEntity.ok("Relatorio gerado com sucesso " + caminho);
    }
    
    @GetMapping(value = "/doacoes")
    public ResponseEntity<String> relatorioDoacoes(@RequestParam String caminho) throws JRException {
    	relatorio.gerarRelatorioDoacaoPDF(caminho);
    	return ResponseEntity.ok("Relatorio gerado com sucesso " + caminho);
    }
}