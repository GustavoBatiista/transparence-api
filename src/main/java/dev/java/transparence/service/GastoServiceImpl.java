package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.GastoRequestDTO;
import dev.java.transparence.dto.GastoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.Gasto;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.GastoRepository;

@Service
public class GastoServiceImpl implements GastoService {
    private static final Logger log = LoggerFactory.getLogger(GastoServiceImpl.class);

    private GastoRepository gastoRepository;

    private ContratoService contratoService;

    public GastoServiceImpl(GastoRepository gastoRepository, ContratoService contratoService) {
        this.gastoRepository = gastoRepository;
        this.contratoService = contratoService;
    }

    @Override
    public GastoResponseDTO incluirGasto(GastoRequestDTO dto) {
        log.info("Iniciando criação de gasto. ContratoId={}", dto.getContratoId());
        Contrato contrato = contratoService.buscarContratoParaOperacoes(dto.getContratoId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de criar um gasto em um contrato não ativo. ContratoId={} | status={}",
                    contrato.getId(), contrato.getStatus());
            throw new BusinessException("Apenas contratos ativos podem gastar");
        }
        boolean existeGasto = gastoRepository.existsByContrato_IdAndDataGastoAndValor(
                dto.getContratoId(), dto.getDataGasto(), dto.getValor());
        if (existeGasto) {
            log.warn("Tentativa de criar um gasto já existente. ContratoId={}", dto.getContratoId());
            throw new BusinessException("Gasto já cadastrado");
        }
        Gasto gasto = new Gasto(contrato,
                dto.getDescricao(), dto.getValor(), dto.getDataGasto());
        Gasto salvo = gastoRepository.save(gasto);
        log.info("Gasto criado com sucesso. GastoId={} | ContratoId={}", salvo.getId(), contrato.getId());
        return toResponseDTO(salvo);
    }

    @Override
    public GastoResponseDTO atualizarGasto(Long id, GastoRequestDTO dto) {
        Gasto gastoExistente = buscarGastoEntityPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        log.info("Iniciando atualização de gasto. GastoId={} | ContratoId={}", id, contrato.getId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de atualizar um gasto em um contrato não ativo. ContratoId={} | status={}",
                    contrato.getId(), contrato.getStatus());
            throw new BusinessException("Apenas gastos de contratos ativos podem ser atualizados");
        }

        gastoExistente.setDescricao(dto.getDescricao());
        gastoExistente.setValor(dto.getValor());
        gastoExistente.setDataGasto(dto.getDataGasto());

        Gasto atualizado = gastoRepository.save(gastoExistente);
        log.info("Gasto atualizado com sucesso. GastoId={} | ContratoId={}", atualizado.getId(),
                atualizado.getContrato().getId());
        return toResponseDTO(atualizado);
    }

    @Override
    public void excluirGasto(Long id) {
        log.info("Iniciando exclusão de gasto. GastoId={}", id);
        Gasto gastoExistente = buscarGastoEntityPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de excluir um gasto em um contrato não ativo. GastoId={} | ContratoId={}", id,
                    contrato.getId());
            throw new BusinessException("Apenas gastos de contratos ativos podem ser excluídos");
        }
        gastoRepository.deleteById(gastoExistente.getId());
        log.info("Gasto excluído com sucesso. GastoId={} | ContratoId={}", id, contrato.getId());
    }


    public Gasto buscarGastoEntityPorId(Long id) {
        log.debug("Buscando gasto da base de dados por GastoId={}", id);
        return gastoRepository.findById(id).orElseThrow(() -> new NotFoundException("Gasto não encontrado"));
    }

    @Override
    public GastoResponseDTO buscarGastoPorId(Long id) {
        log.info("Buscando gasto por GastoId={}", id);
        return toResponseDTO(buscarGastoEntityPorId(id));
    }

    @Override
    public List<GastoResponseDTO> buscarTodosGastos() {
        log.info("Buscando todos os gastos cadastrados.");
        return gastoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private GastoResponseDTO toResponseDTO(Gasto gasto) {
        log.debug("Convertendo gasto para DTO. GastoId={} | ContratoId={}", gasto.getId(), gasto.getContrato().getId());
        GastoResponseDTO dto = new GastoResponseDTO();
        dto.setId(gasto.getId());
        dto.setPessoaCuidadaId(gasto.getContrato().getPessoaCuidada().getId());
        dto.setUsuarioId(gasto.getContrato().getUsuario().getId());
        dto.setContratoId(gasto.getContrato().getId());
        dto.setDescricao(gasto.getDescricao());
        dto.setValor(gasto.getValor());
        dto.setData(gasto.getDataGasto());
        dto.setComprovanteUrl(gasto.getComprovanteUrl());
        return dto;
    }
}
