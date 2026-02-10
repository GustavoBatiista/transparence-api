package dev.java.transparence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.PessoaCuidadaRequestDTO;
import dev.java.transparence.dto.PessoaCuidadaResponseDTO;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.NotFoundException;
import dev.java.transparence.repository.PessoaCuidadaRepository;

@Service
public class PessoaCuidadaServiceImpl implements PessoaCuidadaService {
    private static final Logger log = LoggerFactory.getLogger(PessoaCuidadaServiceImpl.class);

    private PessoaCuidadaRepository pessoaCuidadaRepository;

    public PessoaCuidadaServiceImpl(PessoaCuidadaRepository pessoaCuidadaRepository) {
        this.pessoaCuidadaRepository = pessoaCuidadaRepository;
    }

    @Override
    public PessoaCuidadaResponseDTO incluirPessoaCuidada(PessoaCuidadaRequestDTO dto) {
        log.debug("Iniciando criação de pessoa cuidada. CPF={}", dto.getCpf());
        if (pessoaCuidadaRepository.existsByCpf(dto.getCpf())) {
            log.warn("Tentativa de criar uma pessoa cuidada com CPF já existente.");
            throw new BusinessException("CPF já cadastrado");
        }
        PessoaCuidada pessoaCuidada = new PessoaCuidada(dto.getCpf(), dto.getNome(), dto.getTelefone(),
                dto.getEndereco(), dto.getCidade(), dto.getEstado(), dto.getCep());
        PessoaCuidada salvo = pessoaCuidadaRepository.save(pessoaCuidada);
        log.info("Pessoa cuidada criada com sucesso. PessoaCuidadaId={}", salvo.getId());
        return toResponseDTO(salvo);
    }

    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    @Override
    public PessoaCuidadaResponseDTO atualizarPessoaCuidada(Long id, PessoaCuidadaRequestDTO dto) {
        log.info("Iniciando atualização de pessoa cuidada. PessoaCuidadaId={}", id);
        PessoaCuidada existente = buscarPessoaCuidadaParaContrato(id);
        existente.setNome(dto.getNome());
        existente.setTelefone(dto.getTelefone());
        existente.setEndereco(dto.getEndereco());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setCep(dto.getCep());

        PessoaCuidada salvo = pessoaCuidadaRepository.save(existente);
        log.info("Pessoa cuidada atualizada com sucesso. PessoaCuidadaId={}", salvo.getId());
        return toResponseDTO(salvo);

    }

    @Override
    public void excluirPessoaCuidada(Long id) {
        log.info("Iniciando exclusão de pessoa cuidada. PessoaCuidadaId={}", id);
        if (!pessoaCuidadaRepository.existsById(id)) {
            log.warn("Tentativa de excluir uma pessoa cuidada não existente. PessoaCuidadaId={}", id);
            throw new NotFoundException("Pessoa Cuidada não encontrada");
        }
        pessoaCuidadaRepository.deleteById(id);
        log.info("Pessoa cuidada excluída com sucesso. PessoaCuidadaId={}", id);
    }


    @Override
    public PessoaCuidada buscarPessoaCuidadaParaContrato(Long id) {
        log.debug("Buscando pessoa cuidada da base de dados por PessoaCuidadaId={}", id);
        return pessoaCuidadaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa Cuidada não encontrada"));
    }

    @Override
    public PessoaCuidadaResponseDTO buscarPessoaCuidadaPorId(Long id) {
        log.info("Buscando pessoa cuidada por PessoaCuidadaId={}", id);
        return toResponseDTO(buscarPessoaCuidadaParaContrato(id));
    }

    private PessoaCuidadaResponseDTO toResponseDTO(PessoaCuidada pessoaCuidada) {
        log.debug("Convertendo pessoa cuidada para DTO. PessoaCuidadaId={}", pessoaCuidada.getId());
        PessoaCuidadaResponseDTO dto = new PessoaCuidadaResponseDTO();
        dto.setId(pessoaCuidada.getId());
        dto.setCpf(pessoaCuidada.getCpf());
        dto.setNome(pessoaCuidada.getNome());
        dto.setTelefone(pessoaCuidada.getTelefone());
        dto.setEndereco(pessoaCuidada.getEndereco());
        dto.setCidade(pessoaCuidada.getCidade());
        dto.setEstado(pessoaCuidada.getEstado());
        dto.setCep(pessoaCuidada.getCep());
        return dto;
    }
}
