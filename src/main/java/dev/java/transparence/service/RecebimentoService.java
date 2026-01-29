package dev.java.transparence.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Recebimento;

import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.repository.RecebimentoRepository;

@Service
public class RecebimentoService {

    private RecebimentoRepository recebimentoRepository;

    private PessoaCuidadaService pessoaCuidadaService;

    private UsuarioService usuarioService;

    private ContratoService contratoService;

    public RecebimentoService(RecebimentoRepository recebimentoRepository, PessoaCuidadaService pessoaCuidadaService,
            UsuarioService usuarioService, ContratoService contratoService) {
        this.recebimentoRepository = recebimentoRepository;
        this.pessoaCuidadaService = pessoaCuidadaService;
        this.usuarioService = usuarioService;
        this.contratoService = contratoService;
    }

    public Recebimento incluirRecebimento(Long pessoaCuidadaId, Long usuarioId, Long contratoId,
            String descricao, BigDecimal valor,
            LocalDate dataRecebimento) {
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaPorId(pessoaCuidadaId);
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        Contrato contrato = contratoService.buscarContratoPorId(contratoId);
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas contratos ativos podem receber");
        }
        if (recebimentoRepository.existsLancamentoRecebimento(pessoaCuidadaId, usuarioId,
                dataRecebimento, valor)) {
            throw new RuntimeException("Recebimento já cadastrado");
        }
        Recebimento recebimento = new Recebimento(pessoaCuidada, usuario, contrato, descricao, valor, dataRecebimento);
        return recebimentoRepository.save(recebimento);
    }

    public Recebimento atualizarRecebimento(Long id, String descricao, BigDecimal valor,
            LocalDate dataRecebimento) {
        Recebimento recebimentoExistente = buscarRecebimentoPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas recebimentos de contratos ativos podem ser atualizados");
        }
        recebimentoExistente.setDescricao(descricao);
        recebimentoExistente.setValor(valor);
        recebimentoExistente.setDataRecebimento(dataRecebimento);
        return recebimentoRepository.save(recebimentoExistente);
    }

    public void excluirRecebimento(Long id) {
        Recebimento recebimentoExistente = buscarRecebimentoPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas recebimentos de contratos ativos podem ser excluídos");
        }
        recebimentoRepository.deleteById(recebimentoExistente.getId());
    }

    public Recebimento buscarRecebimentoPorId(Long id) {
        return recebimentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Recebimento não encontrado"));
    }

    public List<Recebimento> buscarTodosRecebimentos() {
        return recebimentoRepository.findAll();
    }
}
