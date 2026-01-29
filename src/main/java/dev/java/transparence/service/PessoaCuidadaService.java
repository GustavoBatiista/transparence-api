package dev.java.transparence.service;

import org.springframework.stereotype.Service;

import dev.java.transparence.entity.PessoaCuidada;
import dev.java.transparence.repository.PessoaCuidadaRepository;

@Service
public class PessoaCuidadaService {

    private PessoaCuidadaRepository pessoaCuidadaRepository;

    public PessoaCuidadaService(PessoaCuidadaRepository pessoaCuidadaRepository) {
        this.pessoaCuidadaRepository = pessoaCuidadaRepository;
    }

    public PessoaCuidada incluirPessoaCuidada(PessoaCuidada pessoaCuidada) {
        if (pessoaCuidadaRepository.existsByCpf(pessoaCuidada.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        return pessoaCuidadaRepository.save(pessoaCuidada);
    }

    public PessoaCuidada atualizarPessoaCuidada(Long id, PessoaCuidada pessoaCuidada) {
        PessoaCuidada existente = buscarPessoaCuidadaPorId(id);
        existente.setNome(pessoaCuidada.getNome());
        existente.setTelefone(pessoaCuidada.getTelefone());
        existente.setEndereco(pessoaCuidada.getEndereco());
        existente.setCidade(pessoaCuidada.getCidade());
        existente.setEstado(pessoaCuidada.getEstado());
        existente.setCep(pessoaCuidada.getCep());

        return pessoaCuidadaRepository.save(pessoaCuidada);

    }

    public void excluirPessoaCuidada(Long id) {
        pessoaCuidadaRepository.deleteById(id);
    }

    public PessoaCuidada buscarPessoaCuidadaPorId(Long id) {
        return pessoaCuidadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa Cuidada não encontrada"));
    }
}
