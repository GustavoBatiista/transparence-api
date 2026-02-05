package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.java.transparence.dto.GastoRequestDTO;
import dev.java.transparence.dto.GastoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.Gasto;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.repository.GastoRepository;

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

    public GastoResponseDTO incluirGasto(GastoRequestDTO dto) {
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaEntityPorId(dto.getPessoaCuidadaId());
        Usuario usuario = usuarioService.buscarUsuarioEntityPorId(dto.getUsuarioId());
        Contrato contrato = contratoService.buscarContratoEntityPorId(dto.getContratoId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas contratos ativos podem gastar");
        }
        boolean existeGasto = gastoRepository.existsByPessoaCuidada_IdAndUsuario_IdAndDataGastoAndValor(
                dto.getPessoaCuidadaId(), dto.getUsuarioId(), dto.getDataGasto(), dto.getValor());
        if (existeGasto) {
            throw new RuntimeException("Gasto já cadastrado");
        }
        Gasto gasto = new Gasto(pessoaCuidada, usuario, contrato,
                dto.getDescricao(), dto.getValor(), dto.getDataGasto());
        Gasto salvo = gastoRepository.save(gasto);

        return toResponseDTO(salvo);
    }

    public GastoResponseDTO atualizarGasto(Long id, GastoRequestDTO dto) {
        Gasto gastoExistente = buscarGastoEntityPorId(id);
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas gastos de contratos ativos podem ser atualizados");
        }

        gastoExistente.setDescricao(dto.getDescricao());
        gastoExistente.setValor(dto.getValor());
        gastoExistente.setDataGasto(dto.getDataGasto());

        Gasto atualizado = gastoRepository.save(gastoExistente);

        return toResponseDTO(atualizado);
    }

    public void excluirGasto(Long id) {
        Gasto gastoExistente = buscarGastoEntityPorId(id);
        if (!gastoRepository.existsById(id)) {
            throw new RuntimeException("Gasto não encontrado");
        }
        Contrato contrato = gastoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas gastos de contratos ativos podem ser excluídos");
        }
        gastoRepository.deleteById(gastoExistente.getId());
    }

    private Gasto buscarGastoEntityPorId(Long id) {
        return gastoRepository.findById(id).orElseThrow(() -> new RuntimeException("Gasto não encontrado"));
    }

    public GastoResponseDTO buscarGastoPorId(Long id) {
        return toResponseDTO(
                gastoRepository.findById(id).orElseThrow(() -> new RuntimeException("Gasto não encontrado")));
    }

    public List<GastoResponseDTO> buscarTodosGastos() {
        return gastoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public GastoResponseDTO toResponseDTO(Gasto gasto) {
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
