package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ong.backend.services.RelatorioService;
import net.sf.jasperreports.engine.JRException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    RelatorioService relatorio;

    // Endpoint para relatorio de usuarios
    @GetMapping(value = "/usuarios", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorioUsuarios(@RequestParam String caminho) throws JRException, IOException {
        // Gerar o relatório
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        relatorio.gerarRelatorioUsuariosPDF(outputStream);

        // Enviar o arquivo gerado como resposta
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_usuarios.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(outputStream.toByteArray());
    }

    // Endpoint para relatorio de doacoes
    @GetMapping(value = "/doacoes", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorioDoacoes(@RequestParam String caminho) throws JRException, IOException {
        // Gerar o relatório
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        relatorio.gerarRelatorioDoacaoPDF(outputStream);

        // Enviar o arquivo gerado como resposta
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_doacoes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(outputStream.toByteArray());
    }
}
