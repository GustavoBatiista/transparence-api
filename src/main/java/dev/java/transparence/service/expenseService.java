package dev.java.transparence.service;
import java.util.List;

import dev.java.transparence.dto.ExpenseRequestDTO;
import dev.java.transparence.dto.ExpenseResponseDTO;




public interface ExpenseService {


    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto);

    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO dto);

    public void deleteExpense(Long id);

    public ExpenseResponseDTO findExpenseById(Long id);

    public List<ExpenseResponseDTO> buscarTodosexpenses();
}
