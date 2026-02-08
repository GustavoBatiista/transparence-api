package dev.java.transparence.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.ContratoRequestDTO;
import dev.java.transparence.dto.ContratoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.ContratoRepository;

@Service
public class ContratoService {

    private static final Logger log = LoggerFactory.getLogger(ContratoService.class);

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
        log.info("Iniciando criação de contrato. UsuarioId={}, PessoaCuidadaId={}",
                dto.getUsuarioId(), dto.getPessoaCuidadaId());

        Usuario usuario = usuarioService.buscarUsuarioEntityPorId(dto.getUsuarioId());
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaEntityPorId(dto.getPessoaCuidadaId());
        if (contratoRepository.existsByUsuario_IdAndPessoaCuidada_IdAndStatus(usuario.getId(), pessoaCuidada.getId(),
                StatusContrato.ATIVO)) {
            log.warn("Tentativa de criar um contrato já existente. UsuarioId={}, PessoaCuidadaId={}",
                    dto.getUsuarioId(), dto.getPessoaCuidadaId());
            throw new BusinessException("Contrato já cadastrado");
        }
        Contrato contrato = new Contrato(usuario, pessoaCuidada, LocalDate.now());
        Contrato salvo = contratoRepository.save(contrato);
        log.info("Contrato criado com sucesso. Id={}", salvo.getId());
        return toResponseDTO(salvo);

    }

    public ContratoResponseDTO encerrarContrato(Long id) {
        log.info("Iniciando encerramento de contrato. Id={}", id);
        Contrato contratoExistente = buscarContratoEntityPorId(id);
        if (contratoExistente.getStatus() == StatusContrato.ENCERRADO) {
            log.warn("Tentativa de encerrar um contrato já encerrado. Id={}", id);
            throw new BusinessException("Contrato já está encerrado");
        }
        contratoExistente.setStatus(StatusContrato.ENCERRADO);
        contratoExistente.setDataFim(LocalDate.now());
        log.info("Contrato encerrado com sucesso.");
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    public ContratoResponseDTO suspenderContrato(Long id) {
        log.info("Iniciando suspensão de contrato. Id={}", id);
        Contrato contratoExistente = buscarContratoEntityPorId(id);
        if (contratoExistente.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de suspender um contrato não ativo. Id={}", id);
            throw new BusinessException("Apenas contratos ativos podem ser suspensos");
        }
        contratoExistente.setStatus(StatusContrato.SUSPENSO);
        log.info("Contrato suspenso com sucesso.");
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    public ContratoResponseDTO reativarContrato(Long id) {
        log.info("Iniciando reativação de contrato. Id={}", id);
        Contrato contratoExistente = buscarContratoEntityPorId(id);
        if (contratoExistente.getStatus() != StatusContrato.SUSPENSO) {
            log.warn("Tentativa de reativar um contrato não suspenso.");
            throw new BusinessException("Apenas contratos suspensos podem ser reativados");
        }
        contratoExistente.setStatus(StatusContrato.ATIVO);
        log.info("Contrato reativado com sucesso.");
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    public void excluirContrato(Long id) {
        log.info("Iniciando exclusão de contrato. Id={}", id);
        if (!contratoRepository.existsById(id)) {
            log.warn("Tentativa de excluir um contrato não existente. Id={}", id);
            throw new NotFoundException("Contrato não encontrado");
        }
        log.info("Contrato excluído com sucesso. Id={}", id);
        contratoRepository.deleteById(id);
    }

    public Contrato buscarContratoEntityPorId(Long id) {
        log.debug("Buscando contrato da base de dados por Id={}", id);
        return contratoRepository.findById(id).orElseThrow(() -> new NotFoundException("Contrato não encontrado"));
    }

    public ContratoResponseDTO buscarContratoPorId(Long id) {
        log.info("Buscando contrato por Id={}", id);
        return toResponseDTO(buscarContratoEntityPorId(id));
    }

    public ContratoResponseDTO toResponseDTO(Contrato contrato) {
        log.debug("Convertendo contrato para DTO. Id={}", contrato.getId());
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
