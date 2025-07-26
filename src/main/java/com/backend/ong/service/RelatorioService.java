package com.backend.ong.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.ong.dto.RelatorioUsuarioDTO;
import com.backend.ong.entity.Usuario;
import com.backend.ong.repositories.UsuarioRepository;
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

    public void gerarRelatorioPDF(String caminho) throws JRException {
        List<Usuario> usuarios = usuarioRepository.findAll();

        List<RelatorioUsuarioDTO> dados = usuarios.stream()
                .map(RelatorioUsuarioDTO::new)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", "Relatorio de Usuarios");

        JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/relatorios1/relatorio_usuarios.jrxml")
        );

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, caminho);
    }
}