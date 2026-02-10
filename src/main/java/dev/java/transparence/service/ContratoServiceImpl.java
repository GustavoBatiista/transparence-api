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
public class ContratoServiceImpl implements ContratoService {

    private static final Logger log = LoggerFactory.getLogger(ContratoServiceImpl.class);

    private ContratoRepository contratoRepository;

    private UsuarioService usuarioService;

    private PessoaCuidadaService pessoaCuidadaService;

    public ContratoServiceImpl(ContratoRepository contratoRepository, UsuarioService usuarioService,
            PessoaCuidadaService pessoaCuidadaService) {
        this.contratoRepository = contratoRepository;
        this.usuarioService = usuarioService;
        this.pessoaCuidadaService = pessoaCuidadaService;
    }

    @Override
    public ContratoResponseDTO incluirContrato(ContratoRequestDTO dto) {
        log.info("Iniciando criação de contrato. UsuarioId={}, PessoaCuidadaId={}",
                dto.getUsuarioId(), dto.getPessoaCuidadaId());

        Usuario usuario = usuarioService.buscarUsuarioParaContrato(dto.getUsuarioId());
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaParaContrato(dto.getPessoaCuidadaId());
        if (contratoRepository.existsByUsuario_IdAndPessoaCuidada_IdAndStatus(usuario.getId(), pessoaCuidada.getId(),
                StatusContrato.ATIVO)) {
            log.warn("Tentativa de criar um contrato já existente. UsuarioId={}, PessoaCuidadaId={}",
                    dto.getUsuarioId(), dto.getPessoaCuidadaId());
            throw new BusinessException("Contrato já cadastrado");
        }
        Contrato contrato = new Contrato(usuario, pessoaCuidada, LocalDate.now());
        Contrato salvo = contratoRepository.save(contrato);
        log.info("Contrato criado com sucesso. ContratoId={} | status={}", salvo.getId(), salvo.getStatus());
        return toResponseDTO(salvo);

    }

    @Override
    public ContratoResponseDTO encerrarContrato(Long id) {
        log.info("Iniciando encerramento de contrato. ContratoId={}", id);
        Contrato contratoExistente = buscarContratoParaOperacoes(id);
        if (contratoExistente.getStatus() == StatusContrato.ENCERRADO) {
            log.warn("Tentativa de encerrar um contrato já encerrado. ContratoId={}", id);
            throw new BusinessException("Contrato já está encerrado");
        }
        contratoExistente.setStatus(StatusContrato.ENCERRADO);
        contratoExistente.setDataFim(LocalDate.now());
        log.info("Contrato encerrado com sucesso. ContratoId={}", id);
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    @Override
    public ContratoResponseDTO suspenderContrato(Long id) {
        log.info("Iniciando suspensão de contrato. ContratoId={}", id);
        Contrato contratoExistente = buscarContratoParaOperacoes(id);
        if (contratoExistente.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de suspender um contrato não ativo. ContratoId={}", id);
            throw new BusinessException("Apenas contratos ativos podem ser suspensos");
        }
        contratoExistente.setStatus(StatusContrato.SUSPENSO);
        log.info("Contrato suspenso com sucesso. ContratoId={}", id);
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    @Override
    public ContratoResponseDTO reativarContrato(Long id) {
        log.info("Iniciando reativação de contrato. ContratoId={}", id);
        Contrato contratoExistente = buscarContratoParaOperacoes(id);
        if (contratoExistente.getStatus() != StatusContrato.SUSPENSO) {
            log.warn("Tentativa de reativar um contrato não suspenso. ContratoId={}", id);
            throw new BusinessException("Apenas contratos suspensos podem ser reativados");
        }
        contratoExistente.setStatus(StatusContrato.ATIVO);
        log.info("Contrato reativado com sucesso. ContratoId={}", id);
        return toResponseDTO(contratoRepository.save(contratoExistente));
    }

    @Override
    public void excluirContrato(Long id) {
        log.info("Iniciando exclusão de contrato. ContratoId={}", id);
        if (!contratoRepository.existsById(id)) {
            log.warn("Tentativa de excluir um contrato não existente. ContratoId={}", id);
            throw new NotFoundException("Contrato não encontrado");
        }
        contratoRepository.deleteById(id);
        log.info("Contrato excluído com sucesso. ContratoId={}", id);
    }

    @Override
    public Contrato buscarContratoParaOperacoes(Long id) {
        log.debug("Buscando contrato para operações. ContratoId={}", id);
        return contratoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contrato não encontrado"));
    }

    @Override
    public ContratoResponseDTO buscarContratoPorId(Long id) {
        log.info("Buscando contrato por ContratoId={}", id);
        return toResponseDTO(buscarContratoParaOperacoes(id));
    }

    private ContratoResponseDTO toResponseDTO(Contrato contrato) {
        log.debug("Convertendo contrato para DTO. ContratoId={}", contrato.getId());
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
