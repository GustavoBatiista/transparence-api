package dev.java.transparence.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.java.transparence.dto.ExpenseRequestDTO;
import dev.java.transparence.dto.ExpenseResponseDTO;
import dev.java.transparence.entity.Contract;
import dev.java.transparence.entity.Expense;
import dev.java.transparence.entity.Dependent;
import dev.java.transparence.entity.User;
import dev.java.transparence.enums.ContractStatus;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.ExpenseRepository;
import dev.java.transparence.service.ContractService;
import dev.java.transparence.service.expenseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class expenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private ContractService contractService;
    @InjectMocks
    private expenseServiceImpl expenseServiceImpl;

    private ExpenseRequestDTO createexpenseRequestDTO() {
        ExpenseRequestDTO dto = new ExpenseRequestDTO();
        dto.setcontractId(1L);
        dto.setDescription("Teste");
        dto.setvalue(BigDecimal.valueOf(100));
        dto.setDataexpense(LocalDate.now());
        return dto;
    }

    private User createUser() {
        User user = new User(
                "12345678901", "Teste", "teste@teste.com", "123456",
                "11999999999", "Rua", "city", "SP", "12345678");

        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    private Dependent createDependent() {
        Dependent pessoa = new Dependent(
                "12345678901", "Teste", "11999999999",
                "Rua", "city", "SP", "12345678");

        ReflectionTestUtils.setField(pessoa, "id", 1L);

        return pessoa;
    }

    private Contract createActiveContract() {

        Contract contract = new Contract(
                createUser(),
                createDependent(),
                LocalDate.now());

        ReflectionTestUtils.setField(contract, "id", 1L);
        ReflectionTestUtils.setField(contract, "status", ContractStatus.ACTIVE);

        return contract;
    }

    private Expense createExpenseEntity() {
        Expense expense = new Expense(
                createActiveContract(),
                "Teste",
                BigDecimal.valueOf(100),
                LocalDate.now());

        ReflectionTestUtils.setField(expense, "id", 1L);

        return expense;
    }

    @Test
    void shouldCreateExpenseSuccessfully() {

        ExpenseRequestDTO dto = createexpenseRequestDTO();
        Contract contract = createActiveContract();

        when(contractService.findContractForOperation(dto.getcontractId()))
                .thenReturn(contract);

        when(expenseRepository.existsBycontract_IdAndDataexpenseAndvalue(
                dto.getcontractId(), dto.getDataexpense(), dto.getvalue())).thenReturn(false);

        when(expenseRepository.save(any())).thenReturn(createExpenseEntity());

        ExpenseResponseDTO response = expenseServiceImpl.createExpense(dto);

        assertEquals(dto.getcontractId(), response.getcontractId());

        verify(contractService).findContractForOperation(dto.getcontractId());
        verify(expenseRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenContractIsNotActive() {
        ExpenseRequestDTO dto = createexpenseRequestDTO();
        Contract contract = createActiveContract();
        contract.setStatus(ContractStatus.CLOSED);

        when(contractService.findContractForOperation(dto.getcontractId()))
                .thenReturn(contract);
        assertThrows(BusinessException.class, () -> expenseServiceImpl.createExpense(dto));

        verify(contractService).findContractForOperation(dto.getcontractId());
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenExpenseAlreadyExists() {
        ExpenseRequestDTO dto = createexpenseRequestDTO();
        Contract contract = createActiveContract();

        when(contractService.findContractForOperation(dto.getcontractId()))
                .thenReturn(contract);
        when(expenseRepository.existsBycontract_IdAndDataexpenseAndvalue(
                dto.getcontractId(), dto.getDataexpense(), dto.getvalue())).thenReturn(true);
        assertThrows(BusinessException.class, () -> expenseServiceImpl.createExpense(dto));

        verify(contractService).findContractForOperation(dto.getcontractId());
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void shouldUpdateExpenseSuccessfully() {
        ExpenseRequestDTO dto = createexpenseRequestDTO();
        Expense expense = createExpenseEntity();

        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        when(expenseRepository.save(any())).thenReturn(expense);

        ExpenseResponseDTO response = expenseServiceImpl.updateExpense(expense.getId(), dto);

        assertEquals(expense.getcontract().getId(), response.getcontractId());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getvalue(), response.getvalue());
        assertEquals(dto.getDataexpense(), response.getData());

        verify(expenseRepository).findById(expense.getId());
        verify(expenseRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingExpenseAndContractIsNotActive() {
        ExpenseRequestDTO dto = createexpenseRequestDTO();
        Expense expense = createExpenseEntity();
        expense.getcontract().setStatus(ContractStatus.CLOSED);

        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));

        assertThrows(BusinessException.class, () -> expenseServiceImpl.updateExpense(expense.getId(), dto));

        verify(expenseRepository).findById(expense.getId());
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingExpense() {
        ExpenseRequestDTO dto = createexpenseRequestDTO();

        when(expenseRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseServiceImpl.updateExpense(dto.getcontractId(), dto));

        verify(expenseRepository).findById(dto.getcontractId());
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void shouldDeleteExpenseSuccessfully() {
        Expense expense = createExpenseEntity();
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        expenseServiceImpl.deleteExpense(expense.getId());
        verify(expenseRepository).findById(expense.getId());
        verify(expenseRepository).deleteById(expense.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingExpenseAndContractIsNotActive() {
        Expense expense = createExpenseEntity();
        expense.getcontract().setStatus(ContractStatus.CLOSED);
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        assertThrows(BusinessException.class, () -> expenseServiceImpl.deleteExpense(expense.getId()));
        verify(expenseRepository).findById(expense.getId());
        verify(expenseRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingExpense() {
        when(expenseRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> expenseServiceImpl.deleteExpense(1L));
        verify(expenseRepository).findById(1L);
        verify(expenseRepository, never()).deleteById(any());
    }

    @Test
    void shouldReturnExpenseListSuccessfully() {
        List<Expense> expenses = new ArrayList<>();
        when(expenseRepository.findAll()).thenReturn(expenses);
        List<ExpenseResponseDTO> response = expenseServiceImpl.buscarTodosexpenses();
        assertEquals(expenses.size(), response.size());
        verify(expenseRepository).findAll();
    }

    @Test
    void shouldReturnExpenseWhenFindingById() {
        Expense expense = createExpenseEntity();
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        ExpenseResponseDTO response = expenseServiceImpl.findExpensById(expense.getId());
        assertEquals(expense.getId(), response.getId());
        verify(expenseRepository).findById(expense.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingExpenseById() {
        when(expenseRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> expenseServiceImpl.findExpensById(1L));
        verify(expenseRepository).findById(any());
    }
}
