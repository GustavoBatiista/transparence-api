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
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.GastoRepository;

@Service
public class GastoService {
    private static final Logger log = LoggerFactory.getLogger(GastoService.class);

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

    public GastoResponseDTO incluirGasto(GastoRequestDTO dto) {
        log.info("Iniciando criação de gasto.");
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaEntityPorId(dto.getPessoaCuidadaId());
        Usuario usuario = usuarioService.buscarUsuarioEntityPorId(dto.getUsuarioId());
        Contrato contrato = contratoService.buscarContratoEntityPorId(dto.getContratoId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de criar um gasto em um contrato não ativo.");
            throw new BusinessException("Apenas contratos ativos podem gastar");
        }
        boolean existeGasto = gastoRepository.existsByPessoaCuidada_IdAndUsuario_IdAndDataGastoAndValor(
                dto.getPessoaCuidadaId(), dto.getUsuarioId(), dto.getDataGasto(), dto.getValor());
        if (existeGasto) {
            log.warn("Tentativa de criar um gasto já existente.");
            throw new BusinessException("Gasto já cadastrado");
        }
        Gasto gasto = new Gasto(pessoaCuidada, usuario, contrato,
                dto.getDescricao(), dto.getValor(), dto.getDataGasto());
        Gasto salvo = gastoRepository.save(gasto);
        log.info("Gasto criado com sucesso. Id={}", salvo.getId());
        return toResponseDTO(salvo);
    }

    public GastoResponseDTO atualizarGasto(Long id, GastoRequestDTO dto) {
        log.info("Iniciando atualização de gasto. Id={}", id);
        Gasto gastoExistente = buscarGastoEntityPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de atualizar um gasto em um contrato não ativo.");
            throw new BusinessException("Apenas gastos de contratos ativos podem ser atualizados");
        }

        gastoExistente.setDescricao(dto.getDescricao());
        gastoExistente.setValor(dto.getValor());
        gastoExistente.setDataGasto(dto.getDataGasto());

        Gasto atualizado = gastoRepository.save(gastoExistente);
        log.info("Gasto atualizado com sucesso. Id={}", atualizado.getId());
        return toResponseDTO(atualizado);
    }

    public void excluirGasto(Long id) {
        log.info("Iniciando exclusão de gasto. Id={}", id);
        Gasto gastoExistente = buscarGastoEntityPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de excluir um gasto em um contrato não ativo. Id={}", id);
            throw new BusinessException("Apenas gastos de contratos ativos podem ser excluídos");
        }
        log.info("Gasto excluído com sucesso. Id={}", id);
        gastoRepository.deleteById(gastoExistente.getId());
    }

    private Gasto buscarGastoEntityPorId(Long id) {
        log.debug("Buscando gasto da base de dados por Id={}", id);
        return gastoRepository.findById(id).orElseThrow(() -> new NotFoundException("Gasto não encontrado"));
    }

    public GastoResponseDTO buscarGastoPorId(Long id) {
        log.info("Buscando gasto por Id={}", id);
        return toResponseDTO(buscarGastoEntityPorId(id));
    }

    public List<GastoResponseDTO> buscarTodosGastos() {
        log.info("Buscando todos os gastos.");
        return gastoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public GastoResponseDTO toResponseDTO(Gasto gasto) {
        log.debug("Convertendo gasto para DTO. Id={}", gasto.getId());
        GastoResponseDTO dto = new GastoResponseDTO();
        dto.setId(gasto.getId());
        dto.setPessoaCuidadaId(gasto.getPessoaCuidada().getId());
        dto.setUsuarioId(gasto.getUsuario().getId());
        dto.setContratoId(gasto.getContrato().getId());
        dto.setDescricao(gasto.getDescricao());
        dto.setValor(gasto.getValor());
        dto.setData(gasto.getDataGasto());
        dto.setComprovanteUrl(gasto.getComprovanteUrl());
        return dto;
    }
}
