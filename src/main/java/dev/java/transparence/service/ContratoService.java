package dev.java.transparence.service;

import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.repository.ContratoRepository;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class ContratoService {

    private ContratoRepository contratoRepository;

    private UsuarioService usuarioService;

    private PessoaCuidadaService pessoaCuidadaService;

    public ContratoService(ContratoRepository contratoRepository, UsuarioService usuarioService,
            PessoaCuidadaService pessoaCuidadaService) {
        this.contratoRepository = contratoRepository;
        this.usuarioService = usuarioService;
        this.pessoaCuidadaService = pessoaCuidadaService;
    }

    public Contrato incluirContrato(Long usuarioId, Long pessoaCuidadaId) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaPorId(pessoaCuidadaId);
        if (contratoRepository.existsByUsuarioIdAndPessoaCuidadaIdAndStatus(usuario.getId(), pessoaCuidada.getId(),
                StatusContrato.ATIVO)) {
            throw new RuntimeException("Contrato já cadastrado");
        }
        Contrato contrato = new Contrato(usuario, pessoaCuidada, LocalDate.now());
        return contratoRepository.save(contrato);
    }

    public Contrato encerrarContrato(Long id) {
        Contrato contratoExistente = buscarContratoPorId(id);
        if (contratoExistente.getStatus() == StatusContrato.ENCERRADO) {
            throw new RuntimeException("Contrato já está encerrado");
        }
        contratoExistente.setStatus(StatusContrato.ENCERRADO);
        contratoExistente.setDataFim(LocalDate.now());
        return contratoRepository.save(contratoExistente);
    }

    public Contrato suspenderContrato(Long id) {
        Contrato contratoExistente = buscarContratoPorId(id);
        if (contratoExistente.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas contratos ativos podem ser suspensos");
        }
        contratoExistente.setStatus(StatusContrato.SUSPENSO);
        return contratoRepository.save(contratoExistente);
    }

    public Contrato reativarContrato(Long id) {
        Contrato contratoExistente = buscarContratoPorId(id);
        if (contratoExistente.getStatus() != StatusContrato.SUSPENSO) {
            throw new RuntimeException("Apenas contratos suspensos podem ser reativados");
        }
        contratoExistente.setStatus(StatusContrato.ATIVO);
        return contratoRepository.save(contratoExistente);
    }

    public void excluirContrato(Long id) {
        contratoRepository.deleteById(id);
    }

    public Contrato buscarContratoPorId(Long id) {
        return contratoRepository.findById(id).orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
    }
}
