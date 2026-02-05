package dev.java.transparence.service;

import org.springframework.stereotype.Service;

import dev.java.transparence.dto.PessoaCuidadaRequestDTO;
import dev.java.transparence.dto.PessoaCuidadaResponseDTO;
import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.repository.PessoaCuidadaRepository;

@Service
public class PessoaCuidadaService {

    private PessoaCuidadaRepository pessoaCuidadaRepository;

    public PessoaCuidadaService(PessoaCuidadaRepository pessoaCuidadaRepository) {
        this.pessoaCuidadaRepository = pessoaCuidadaRepository;
    }

    public PessoaCuidadaResponseDTO incluirPessoaCuidada(PessoaCuidadaRequestDTO dto) {
        if (pessoaCuidadaRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        PessoaCuidada pessoaCuidada = new PessoaCuidada(dto.getCpf(), dto.getNome(), dto.getTelefone(),
                dto.getEndereco(), dto.getCidade(), dto.getEstado(), dto.getCep());
        return toResponseDTO(pessoaCuidadaRepository.save(pessoaCuidada));
    }

    public PessoaCuidadaResponseDTO atualizarPessoaCuidada(Long id, PessoaCuidadaRequestDTO dto) {
        PessoaCuidada existente = buscarPessoaCuidadaEntityPorId(id);
        existente.setNome(dto.getNome());
        existente.setTelefone(dto.getTelefone());
        existente.setEndereco(dto.getEndereco());
        existente.setCidade(dto.getCidade());
        existente.setEstado(dto.getEstado());
        existente.setCep(dto.getCep());

        return toResponseDTO(pessoaCuidadaRepository.save(existente));

    }

    public void excluirPessoaCuidada(Long id) {
        if (!pessoaCuidadaRepository.existsById(id)) {
            throw new RuntimeException("Pessoa Cuidada não encontrada");
        }
        pessoaCuidadaRepository.deleteById(id);
    }

    public PessoaCuidada buscarPessoaCuidadaEntityPorId(Long id) {
        return pessoaCuidadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa Cuidada não encontrada"));
    }

    public PessoaCuidadaResponseDTO buscarPessoaCuidadaPorId(Long id) {
        return toResponseDTO(buscarPessoaCuidadaEntityPorId(id));
    }

    public PessoaCuidadaResponseDTO toResponseDTO(PessoaCuidada pessoaCuidada) {
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
