package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.java.transparence.dto.RecebimentoRequestDTO;
import dev.java.transparence.dto.RecebimentoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Recebimento;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
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

    public RecebimentoResponseDTO incluirRecebimento(RecebimentoRequestDTO dto) {
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaEntityPorId(dto.getPessoaCuidadaId());
        Usuario usuario = usuarioService.buscarUsuarioEntityPorId(dto.getUsuarioId());
        Contrato contrato = contratoService.buscarContratoEntityPorId(dto.getContratoId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas contratos ativos podem receber");
        }
        boolean existeRecebimento = recebimentoRepository
                .existsByPessoaCuidada_IdAndUsuario_IdAndDataRecebimentoAndValor(dto.getPessoaCuidadaId(),
                        dto.getUsuarioId(), dto.getDataRecebimento(), dto.getValor());
        if (existeRecebimento) {
            throw new RuntimeException("Recebimento já cadastrado");
        }
        Recebimento recebimento = new Recebimento(pessoaCuidada, usuario, contrato, dto.getDescricao(), dto.getValor(),
                dto.getDataRecebimento());
        Recebimento salvo = recebimentoRepository.save(recebimento);
        return toResponseDTO(salvo);
    }

    public RecebimentoResponseDTO atualizarRecebimento(Long id, RecebimentoRequestDTO dto) {
        Recebimento recebimentoExistente = buscarRecebimentoEntityPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas recebimentos de contratos ativos podem ser atualizados");
        }
        recebimentoExistente.setDescricao(dto.getDescricao());
        recebimentoExistente.setValor(dto.getValor());
        recebimentoExistente.setDataRecebimento(dto.getDataRecebimento());
        return toResponseDTO(recebimentoRepository.save(recebimentoExistente));
    }

    public void excluirRecebimento(Long id) {
        Recebimento recebimentoExistente = buscarRecebimentoEntityPorId(id);
        if (!recebimentoRepository.existsById(id)) {
            throw new RuntimeException("Recebimento não encontrado");
        }
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            throw new RuntimeException("Apenas recebimentos de contratos ativos podem ser excluídos");
        }
        recebimentoRepository.deleteById(recebimentoExistente.getId());
    }

    private Recebimento buscarRecebimentoEntityPorId(Long id) {
        return recebimentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Recebimento não encontrado"));
    }

    public RecebimentoResponseDTO buscarRecebimentoPorId(Long id) {
        return toResponseDTO(recebimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recebimento não encontrado")));
    }

    public List<RecebimentoResponseDTO> buscarTodosRecebimentos() {
        return recebimentoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public RecebimentoResponseDTO toResponseDTO(Recebimento recebimento) {
        RecebimentoResponseDTO dto = new RecebimentoResponseDTO();
        dto.setId(recebimento.getId());
        dto.setPessoaCuidadaId(recebimento.getPessoaCuidada().getId());
        dto.setUsuarioId(recebimento.getUsuario().getId());
        dto.setContratoId(recebimento.getContrato().getId());
        dto.setDescricao(recebimento.getDescricao());
        dto.setValor(recebimento.getValor());
        dto.setData(recebimento.getDataRecebimento());
        dto.setComprovanteUrl(recebimento.getComprovanteUrl());
        return dto;
    }
}
