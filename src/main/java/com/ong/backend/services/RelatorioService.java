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

    public void gerarRelatorioUsuariosPDF(ByteArrayOutputStream outputStream) throws JRException {
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        List<RelatorioUsuarioDTO> dados = usuarios.stream()
                .map(RelatorioUsuarioDTO::new)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", "Relatório de Usuários");

        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/relatorios/relatorio_usuarios.jrxml")
        );

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    public void gerarRelatorioDoacaoPDF(ByteArrayOutputStream outputStream) throws JRException {
        List<Doacao> doacoes = doacaoRepository.findAll();

        List<RelatorioDoacaoDTO> dados = doacoes.stream()
                .map(RelatorioDoacaoDTO::new)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", "Relatório de Doações");

        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/relatorios/relatorio_doacao.jrxml")
        );

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
}
