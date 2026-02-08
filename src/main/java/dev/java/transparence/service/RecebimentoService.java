package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.RecebimentoRequestDTO;
import dev.java.transparence.dto.RecebimentoResponseDTO;
import dev.java.transparence.entity.Contrato;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.entity.Recebimento;
import dev.java.transparence.entity.Usuario;
import dev.java.transparence.enums.StatusContrato;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.RecebimentoRepository;

@Service
public class RecebimentoService {

    private static final Logger log = LoggerFactory.getLogger(RecebimentoService.class);

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
        log.info("Iniciando criação de recebimento.");
        PessoaCuidada pessoaCuidada = pessoaCuidadaService.buscarPessoaCuidadaEntityPorId(dto.getPessoaCuidadaId());
        Usuario usuario = usuarioService.buscarUsuarioEntityPorId(dto.getUsuarioId());
        Contrato contrato = contratoService.buscarContratoEntityPorId(dto.getContratoId());
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de criar um recebimento em um contrato não ativo.");
            throw new BusinessException("Apenas contratos ativos podem receber");
        }
        boolean existeRecebimento = recebimentoRepository
                .existsByPessoaCuidada_IdAndUsuario_IdAndDataRecebimentoAndValor(dto.getPessoaCuidadaId(),
                        dto.getUsuarioId(), dto.getDataRecebimento(), dto.getValor());
        if (existeRecebimento) {
            log.warn("Tentativa de criar um recebimento já existente.");
            throw new BusinessException("Recebimento já cadastrado");
        }
        Recebimento recebimento = new Recebimento(pessoaCuidada, usuario, contrato, dto.getDescricao(), dto.getValor(),
                dto.getDataRecebimento());
        Recebimento salvo = recebimentoRepository.save(recebimento);
        log.info("Recebimento criado com sucesso. Id={}", salvo.getId());
        return toResponseDTO(salvo);
    }

    public RecebimentoResponseDTO atualizarRecebimento(Long id, RecebimentoRequestDTO dto) {
        log.info("Iniciando atualização de recebimento. Id={}", id);
        Recebimento recebimentoExistente = buscarRecebimentoEntityPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de atualizar um recebimento em um contrato não ativo. Id={}", id);
            throw new BusinessException("Apenas recebimentos de contratos ativos podem ser atualizados");
        }
        recebimentoExistente.setDescricao(dto.getDescricao());
        recebimentoExistente.setValor(dto.getValor());
        recebimentoExistente.setDataRecebimento(dto.getDataRecebimento());

        Recebimento atualizado = recebimentoRepository.save(recebimentoExistente);
        log.info("Recebimento atualizado com sucesso. Id={}", atualizado.getId());
        return toResponseDTO(atualizado);
    }

    public void excluirRecebimento(Long id) {
        log.info("Iniciando exclusão de recebimento. Id={}", id);
        Recebimento recebimentoExistente = buscarRecebimentoEntityPorId(id);
        Contrato contrato = recebimentoExistente.getContrato();
        if (contrato.getStatus() != StatusContrato.ATIVO) {
            log.warn("Tentativa de excluir um recebimento em um contrato não ativo. Id={}", id);
            throw new BusinessException("Apenas recebimentos de contratos ativos podem ser excluídos");
        }
        recebimentoRepository.deleteById(recebimentoExistente.getId());
    }

    private Recebimento buscarRecebimentoEntityPorId(Long id) {
        log.debug("Buscando recebimento da base de dados por Id={}", id);
        return recebimentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recebimento não encontrado"));
    }

    public RecebimentoResponseDTO buscarRecebimentoPorId(Long id) {
        log.info("Buscando recebimento por Id={}", id);
        return toResponseDTO(buscarRecebimentoEntityPorId(id));
    }

    public List<RecebimentoResponseDTO> buscarTodosRecebimentos() {
        log.info("Buscando todos os recebimentos.");
        return recebimentoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public RecebimentoResponseDTO toResponseDTO(Recebimento recebimento) {
        log.debug("Convertendo recebimento para DTO. Id={}", recebimento.getId());
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
