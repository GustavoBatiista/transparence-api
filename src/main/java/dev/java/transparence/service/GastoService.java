package dev.java.transparence.service;

import dev.java.transparence.repository.GastoRepository;
import org.springframework.stereotype.Service;

import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.Gasto;
import dev.java.transparence.enums.StatusContrato;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class GastoService {

    private GastoRepository gastoRepository;

    private PessoaCuidadaService pessoaCuidadaService;

    private UsuarioService usuarioService;

    private ContratoService contratoService;

    public GastoService(GastoRepository gastoRepository, PessoaCuidadaService pessoaCuidadaService,
            UsuarioService usuarioService, ContratoService contratoService) {
        this.gastoRepository = gastoRepository;
        this.pessoaCuidadaService = pessoaCuidadaService;
        this.usuarioService = usuarioService;
        this.contratoService = contratoService;
    }

    public Gasto incluirGasto(Long pessoaCuidadaId, Long usuarioId, Long contratoId,
            String descricao, BigDecimal valor,
            LocalDate dataGasto) {
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaPorId(pessoaCuidadaId);
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        Contrato contrato = contratoService.buscarContratoPorId(contratoId);
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas contratos ativos podem gastar");
        }
        if (gastoRepository.existsLancamentoGasto(pessoaCuidadaId, usuarioId,
                dataGasto, valor)) {
            throw new RuntimeException("Gasto já cadastrado");
        }
        Gasto gasto = new Gasto(pessoaCuidada, usuario, contrato, descricao, valor, dataGasto);
        return gastoRepository.save(gasto);
    }

    public Gasto atualizarGasto(Long id, String descricao, BigDecimal valor,
            LocalDate dataGasto) {
        Gasto gastoExistente = buscarGastoPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas gastos de contratos ativos podem ser atualizados");
        }
        gastoExistente.setDescricao(descricao);
        gastoExistente.setValor(valor);
        gastoExistente.setDataGasto(dataGasto);
        return gastoRepository.save(gastoExistente);
    }

    public void excluirGasto(Long id) {
        Gasto gastoExistente = buscarGastoPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas gastos de contratos ativos podem ser excluídos");
        }
        gastoRepository.deleteById(gastoExistente.getId());
    }

    public Gasto buscarGastoPorId(Long id) {
        return gastoRepository.findById(id).orElseThrow(() -> new RuntimeException("Gasto não encontrado"));
    }

    public List<Gasto> buscarTodosGastos() {
        return gastoRepository.findAll();
    }
}
