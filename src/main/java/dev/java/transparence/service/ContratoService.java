package dev.java.transparence.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import dev.java.transparence.dto.ContratoRequestDTO;
import dev.java.transparence.dto.ContratoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.repository.ContratoRepository;

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

    public ContratoResponseDTO incluirContrato(ContratoRequestDTO dto) {
        Usuario usuario = usuarioService.buscarUsuarioEntityPorId(dto.getUsuarioId());
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaEntityPorId(dto.getPessoaCuidadaId());
        if (contratoRepository.existsByUsuario_IdAndPessoaCuidada_IdAndStatus(usuario.getId(), pessoaCuidada.getId(),
                StatusContrato.ATIVO)) {
            throw new RuntimeException("Contrato já cadastrado");
        }
        Contrato contrato = new Contrato(usuario, pessoaCuidada, LocalDate.now());
        return toResponseDTO(contratoRepository.save(contrato));
    }

    public ContratoResponseDTO encerrarContrato(Long id) {
        Contrato contratoExistente = buscarContratoEntityPorId(id);
        if (contratoExistente.getStatus() == StatusContrato.ENCERRADO) {
            throw new RuntimeException("Contrato já está encerrado");
        }
        contratoExistente.setStatus(StatusContrato.ENCERRADO);
        contratoExistente.setDataFim(LocalDate.now());
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    public ContratoResponseDTO suspenderContrato(Long id) {
        Contrato contratoExistente = buscarContratoEntityPorId(id);
        if (contratoExistente.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas contratos ativos podem ser suspensos");
        }
        contratoExistente.setStatus(StatusContrato.SUSPENSO);
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    public ContratoResponseDTO reativarContrato(Long id) {
        Contrato contratoExistente = buscarContratoEntityPorId(id);
        if (contratoExistente.getStatus() != StatusContrato.SUSPENSO) {
            throw new RuntimeException("Apenas contratos suspensos podem ser reativados");
        }
        contratoExistente.setStatus(StatusContrato.ATIVO);
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    public void excluirContrato(Long id) {
        if (!contratoRepository.existsById(id)) {
            throw new RuntimeException("Contrato não encontrado");
        }
        contratoRepository.deleteById(id);
    }

    public Contrato buscarContratoEntityPorId(Long id) {
        return contratoRepository.findById(id).orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
    }

    public ContratoResponseDTO buscarContratoPorId(Long id) {
        return toResponseDTO(buscarContratoEntityPorId(id));
    }

    public ContratoResponseDTO toResponseDTO(Contrato contrato) {
        ContratoResponseDTO dto = new ContratoResponseDTO();
        dto.setId(contrato.getId());
        dto.setUsuarioId(contrato.getUsuario().getId());
        dto.setPessoaCuidadaId(contrato.getPessoaCuidada().getId());
        dto.setDataInicio(contrato.getDataInicio());
        dto.setDataFim(contrato.getDataFim());
        dto.setStatus(contrato.getStatus());
        return dto;
    }
}
