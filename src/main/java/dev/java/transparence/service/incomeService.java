package dev.java.transparence.service;

import java.util.List;

import dev.java.transparence.dto.IncomeRequestDTO;
import dev.java.transparence.dto.IncomeResponseDTO;


public interface incomeService {

    public IncomeResponseDTO createIncome(IncomeRequestDTO dto);

    public IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO dto);

    public void deleteIncome(Long id);

    public IncomeResponseDTO findIncomeById(Long id);

    public List<IncomeResponseDTO> findAllIncome();
}