package dev.java.transparence.service;

import dev.java.transparence.dto.ContratoRequestDTO;
import dev.java.transparence.dto.ContratoResponseDTO;
import dev.java.transparence.entity.Contrato;


public interface ContratoService {

    Contrato buscarContratoParaOperacoes(Long contratoId);

    public ContratoResponseDTO incluirContrato(ContratoRequestDTO dto);

    public ContratoResponseDTO encerrarContrato(Long id);

    public ContratoResponseDTO suspenderContrato(Long id);

    public ContratoResponseDTO reativarContrato(Long id); 

    public void excluirContrato(Long id); 

    public ContratoResponseDTO buscarContratoPorId(Long id);

}