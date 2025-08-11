package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.PagamentoDTO;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.Pagamento;
import com.ong.backend.entities.StatusPagamento;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.InscricaoRepository;
import com.ong.backend.repositories.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    InscricaoRepository inscricaoRepository;

    public ResponseEntity<?> pagar(PagamentoDTO dto) {
        Inscricao inscricao = inscricaoRepository.findById(dto.getIdInscricao())
                .orElseThrow(() -> new NaoEncontradoException("Inscrição não encontrada"));

        boolean jaPago = pagamentoRepository.existsByInscricao(inscricao);
        if (jaPago) {
            return ResponseEntity.badRequest()
                .body(new MensagemResponse("Pagamento já realizado para esta inscrição"));
        }

        float valorCurso = inscricao.getIdCurso().getValor();
        if (dto.getValorPago() != valorCurso) {
            return ResponseEntity.badRequest()
                .body(new MensagemResponse("O valor do pagamento deve ser igual ao valor do curso: " + valorCurso));
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setValorPago(dto.getValorPago());
        pagamento.setInscricao(inscricao);

        pagamento = pagamentoRepository.save(pagamento);

        inscricao.setStatusPagamento(StatusPagamento.PAGO);
        inscricaoRepository.save(inscricao);

        return ResponseEntity.ok(pagamento);
    }

    public List<Pagamento> listar() {
        return pagamentoRepository.findAll();
    }
}
