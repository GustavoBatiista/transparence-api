package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.RecebimentoRequestDTO;
import dev.java.transparence.dto.RecebimentoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.Recebimento;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.RecebimentoRepository;

@Service
public class RecebimentoServiceImpl implements RecebimentoService {

    private static final Logger log = LoggerFactory.getLogger(RecebimentoServiceImpl.class);

    private RecebimentoRepository recebimentoRepository;

    private ContratoService contratoService;

    public RecebimentoServiceImpl(RecebimentoRepository recebimentoRepository, ContratoService contratoService) {
        this.recebimentoRepository = recebimentoRepository;
        this.contratoService = contratoService;
    }

    @Override
    public RecebimentoResponseDTO incluirRecebimento(RecebimentoRequestDTO dto) {
        log.info("Iniciando criação de recebimento. ContratoId={}", dto.getContratoId());
        Contrato contrato = contratoService.buscarContratoParaOperacoes(dto.getContratoId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de criar um recebimento em um contrato não ativo. Id={}", dto.getContratoId());
            throw new BusinessException("Apenas contratos ativos podem receber");
        }
        boolean existeRecebimento = recebimentoRepository
                .existsByContrato_IdAndDataRecebimentoAndValor(dto.getContratoId(), dto.getDataRecebimento(),
                        dto.getValor());
        if (existeRecebimento) {
            log.warn("Tentativa de criar um recebimento já existente. ContratoId={}", dto.getContratoId());
            throw new BusinessException("Recebimento já cadastrado");
        }
        Recebimento recebimento = new Recebimento(contrato, dto.getDescricao(), dto.getValor(),
                dto.getDataRecebimento());
        Recebimento salvo = recebimentoRepository.save(recebimento);
        log.info("Recebimento criado com sucesso. RecebimentoId={} | ContratoId={}", salvo.getId(), dto.getContratoId());
        return toResponseDTO(salvo);
    }

    @Override
    public RecebimentoResponseDTO atualizarRecebimento(Long id, RecebimentoRequestDTO dto) {
        Recebimento recebimentoExistente = buscarRecebimentoEntityPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        log.info("Iniciando atualização de recebimento. RecebimentoId={} | ContratoId={}", id, contrato.getId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de atualizar um recebimento em um contrato não ativo. ContratoId={} | status={}", contrato.getId(), contrato.getStatus());
            throw new BusinessException("Apenas recebimentos de contratos ativos podem ser atualizados");
        }
        recebimentoExistente.setDescricao(dto.getDescricao());
        recebimentoExistente.setValor(dto.getValor());
        recebimentoExistente.setDataRecebimento(dto.getDataRecebimento());

        Recebimento atualizado = recebimentoRepository.save(recebimentoExistente);
        log.info("Recebimento atualizado com sucesso. RecebimentoId={} | ContratoId={}", atualizado.getId(), atualizado.getContrato().getId());
        return toResponseDTO(atualizado);
    }

    @Override
    public void excluirRecebimento(Long id) {
        log.info("Iniciando exclusão de recebimento. RecebimentoId={}", id);
        Recebimento recebimentoExistente = buscarRecebimentoEntityPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de excluir um recebimento em um contrato não ativo. ContratoId={} | status={}", contrato.getId(), contrato.getStatus());
            throw new BusinessException("Apenas recebimentos de contratos ativos podem ser excluídos");
        }
        recebimentoRepository.deleteById(recebimentoExistente.getId());
        log.info("Recebimento excluído com sucesso. RecebimentoId={} | ContratoId={}", id, contrato.getId());
    }

    public Recebimento buscarRecebimentoEntityPorId(Long id) {
        log.debug("Buscando recebimento da base de dados por RecebimentoId={}", id);
        return recebimentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recebimento não encontrado"));
    }

    @Override
    public RecebimentoResponseDTO buscarRecebimentoPorId(Long id) {
        log.info("Buscando recebimento por RecebimentoId={}", id);
        return toResponseDTO(buscarRecebimentoEntityPorId(id));
    }

    @Override
    public List<RecebimentoResponseDTO> buscarTodosRecebimentos() {
        log.info("Buscando todos os recebimentos cadastrados.");
        return recebimentoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private RecebimentoResponseDTO toResponseDTO(Recebimento recebimento) {
        log.debug("Convertendo recebimento para DTO. RecebimentoId={} | ContratoId={}", recebimento.getId(), recebimento.getContrato().getId());
        RecebimentoResponseDTO dto = new RecebimentoResponseDTO();
        dto.setId(recebimento.getId());
        dto.setPessoaCuidadaId(recebimento.getContrato().getPessoaCuidada().getId());
        dto.setUsuarioId(recebimento.getContrato().getUsuario().getId());
        dto.setContratoId(recebimento.getContrato().getId());
        dto.setDescricao(recebimento.getDescricao());
        dto.setValor(recebimento.getValor());
        dto.setData(recebimento.getDataRecebimento());
        dto.setComprovanteUrl(recebimento.getComprovanteUrl());
        return dto;
    }
}
