package dev.java.transparence.service;
import java.util.List;

import dev.java.transparence.dto.GastoRequestDTO;
import dev.java.transparence.dto.GastoResponseDTO;
import dev.java.transparence.entity.Gasto;



public interface GastoService {


    public GastoResponseDTO incluirGasto(GastoRequestDTO dto);

    public GastoResponseDTO atualizarGasto(Long id, GastoRequestDTO dto);

    public void excluirGasto(Long id);

    public GastoResponseDTO buscarGastoPorId(Long id);

    public List<GastoResponseDTO> buscarTodosGastos();
}
