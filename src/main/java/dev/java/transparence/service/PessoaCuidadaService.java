package dev.java.transparence.service;


import dev.java.transparence.dto.PessoaCuidadaRequestDTO;
import dev.java.transparence.dto.PessoaCuidadaResponseDTO;
import dev.java.transparence.entity.PessoaCuidada;


public interface PessoaCuidadaService {

    public PessoaCuidada buscarPessoaCuidadaParaContrato(Long id);

    public PessoaCuidadaResponseDTO incluirPessoaCuidada(PessoaCuidadaRequestDTO dto);
    // TODO: separar DTO de criação e atualização futuramente 09/02/2026
    public PessoaCuidadaResponseDTO atualizarPessoaCuidada(Long id, PessoaCuidadaRequestDTO dto);

    public void excluirPessoaCuidada(Long id);

    public PessoaCuidadaResponseDTO buscarPessoaCuidadaPorId(Long id);
}
