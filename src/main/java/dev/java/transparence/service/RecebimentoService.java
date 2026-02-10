package dev.java.transparence.service;

import java.util.List;

import dev.java.transparence.dto.RecebimentoRequestDTO;
import dev.java.transparence.dto.RecebimentoResponseDTO;


public interface RecebimentoService {

    public RecebimentoResponseDTO incluirRecebimento(RecebimentoRequestDTO dto);

    public RecebimentoResponseDTO atualizarRecebimento(Long id, RecebimentoRequestDTO dto);

    public void excluirRecebimento(Long id);

    public RecebimentoResponseDTO buscarRecebimentoPorId(Long id);

    public List<RecebimentoResponseDTO> buscarTodosRecebimentos();
}