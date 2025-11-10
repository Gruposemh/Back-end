// RelatorioService.java
package com.ong.backend.services;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.RelatorioDoacaoDTO;
import com.ong.backend.dto.RelatorioUsuarioDTO;
import com.ong.backend.entities.Doacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.DoacaoRepository;
import com.ong.backend.repositories.UsuarioRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RelatorioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DoacaoRepository doacaoRepository;

    // Gera o relatório de usuários e escreve no outputStream
    public void gerarRelatorioUsuariosPDF(ByteArrayOutputStream outputStream) throws JRException {
        // Buscando usuários do banco
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        if (usuarios.isEmpty()) {
            throw new JRException("Nenhum usuário encontrado para gerar o relatório.");
        }

        // Convertendo para DTOs
        List<RelatorioUsuarioDTO> dados = usuarios.stream()
                .map(RelatorioUsuarioDTO::new)
                .collect(Collectors.toList());

        // Fonte de dados do Jasper
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", "Relatório de Usuários");

        // Compilando o arquivo .jrxml para gerar o relatório
        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/relatorios/relatorio_usuarios.jrxml")
        );

        // Preenchendo o relatório com os dados e parâmetros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        // Gerando o PDF no outputStream
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    // Gera o relatório de doações e escreve no outputStream
    public void gerarRelatorioDoacaoPDF(ByteArrayOutputStream outputStream) throws JRException {
        // Buscando doações do banco
        List<Doacao> doacoes = doacaoRepository.findAll();

        if (doacoes.isEmpty()) {
            throw new JRException("Nenhuma doação encontrada para gerar o relatório.");
        }

        // Convertendo para DTOs
        List<RelatorioDoacaoDTO> dados = doacoes.stream()
                .map(RelatorioDoacaoDTO::new)
                .collect(Collectors.toList());

        // Fonte de dados do Jasper
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", "Relatório de Doações");

        // Compilando o arquivo .jrxml para gerar o relatório
        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/relatorios/relatorio_doacao.jrxml")
        );

        // Preenchendo o relatório com os dados e parâmetros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        // Gerando o PDF no outputStream
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
}
