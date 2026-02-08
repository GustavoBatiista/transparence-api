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
public class PessoaCuidadaService {
    private static final Logger log = LoggerFactory.getLogger(PessoaCuidadaService.class);

    private PessoaCuidadaRepository pessoaCuidadaRepository;

    public PessoaCuidadaService(PessoaCuidadaRepository pessoaCuidadaRepository) {
        this.pessoaCuidadaRepository = pessoaCuidadaRepository;
    }

    public PessoaCuidadaResponseDTO incluirPessoaCuidada(PessoaCuidadaRequestDTO dto) {
        log.info("Iniciando criação de pessoa cuidada.");
        if (pessoaCuidadaRepository.existsByCpf(dto.getCpf())) {
            log.warn("Tentativa de criar uma pessoa cuidada com CPF já existente.");
            throw new BusinessException("CPF já cadastrado");
        }
        PessoaCuidada pessoaCuidada = new PessoaCuidada(dto.getCpf(), dto.getNome(), dto.getTelefone(),
                dto.getEndereco(), dto.getCidade(), dto.getEstado(), dto.getCep());
        PessoaCuidada salvo = pessoaCuidadaRepository.save(pessoaCuidada);
        log.info("Pessoa cuidada criada com sucesso. Id={}", salvo.getId());
        return toResponseDTO(salvo);
    }

    public PessoaCuidadaResponseDTO atualizarPessoaCuidada(Long id, PessoaCuidadaRequestDTO dto) {
        log.info("Iniciando atualização de pessoa cuidada. Id={}", id);
        PessoaCuidada existente = buscarPessoaCuidadaEntityPorId(id);
        existente.setNome(dto.getNome());
        existente.setTelefone(dto.getTelefone());
        existente.setEndereco(dto.getEndereco());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setCep(dto.getCep());

        PessoaCuidada salvo = pessoaCuidadaRepository.save(existente);
        log.info("Pessoa cuidada atualizada com sucesso. Id={}", salvo.getId());
        return toResponseDTO(salvo);

    }

    public void excluirPessoaCuidada(Long id) {
        log.info("Iniciando exclusão de pessoa cuidada. Id={}", id);
        if (!pessoaCuidadaRepository.existsById(id)) {
            log.warn("Tentativa de excluir uma pessoa cuidada não existente. Id={}", id);
            throw new NotFoundException("Pessoa Cuidada não encontrada");
        }
        log.info("Pessoa cuidada excluída com sucesso. Id={}", id);
        pessoaCuidadaRepository.deleteById(id);
    }

    public PessoaCuidada buscarPessoaCuidadaEntityPorId(Long id) {
        log.debug("Buscando pessoa cuidada da base de dados por Id={}", id);
        return pessoaCuidadaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa Cuidada não encontrada"));
    }

    public PessoaCuidadaResponseDTO buscarPessoaCuidadaPorId(Long id) {
        log.info("Buscando pessoa cuidada por Id={}", id);
        return toResponseDTO(buscarPessoaCuidadaEntityPorId(id));
    }

    public PessoaCuidadaResponseDTO toResponseDTO(PessoaCuidada pessoaCuidada) {
        log.debug("Convertendo pessoa cuidada para DTO. Id={}", pessoaCuidada.getId());
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
