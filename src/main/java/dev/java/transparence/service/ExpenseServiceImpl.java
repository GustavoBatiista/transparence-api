package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.ExpenseRequestDTO;
import dev.java.transparence.dto.ExpenseResponseDTO;
import dev.java.transparence.entity.Contract;
import dev.java.transparence.entity.Expense;
import dev.java.transparence.enums.ContractStatus;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private ExpenseRepository expenseRepository;
    private ContractService contractService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ContractService contractService) {
        this.expenseRepository = expenseRepository;
        this.contractService = contractService;
    }

    @Override
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto) {

        log.info("Starting expense creation. contractId={}", dto.getContractId());

        Contract contract = contractService.findContractForOperation(dto.getContractId());

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to create an expense for a non-active contract. contractId={} | status={}",
                    contract.getId(), contract.getStatus());
            throw new BusinessException("Only expenses from active contracts are allowed");
        }

        boolean existeExpense = expenseRepository.existsByContract_IdAndDataExpenseAndValue(
                dto.getContractId(), dto.getDataExpense(), dto.getValue());

        if (existeExpense) {
            log.warn("Attempt to create an already existing expense. contractId={}", dto.getContractId());
            throw new BusinessException("Expense already exists");
        }

        Expense expense = new Expense(contract,
                dto.getDescription(), dto.getValue(), dto.getDataExpense());

        Expense save = expenseRepository.save(expense);

        log.info("Expense created successfully. expenseId={} | contractId={}", save.getId(), contract.getId());

        return toResponseDTO(save);
    }

    @Override
    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO dto) {

        Expense expenseExistente = findExpenseEntityById(id);
        Contract contract = expenseExistente.getContract();

        log.info("Starting expense update. expenseId={} | contractId={}", id, contract.getId());

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to update an expense for a non-active contract. contractId={} | status={}",
                    contract.getId(), contract.getStatus());
            throw new BusinessException("Only expenses from active contracts can be updated");
        }

        expenseExistente.setDescription(dto.getDescription());
        expenseExistente.setValue(dto.getValue());
        expenseExistente.setDataExpense(dto.getDataExpense());

        Expense atualizado = expenseRepository.save(expenseExistente);

        log.info("Expense updated successfully. expenseId={} | contractId={}",
                atualizado.getId(), atualizado.getContract().getId());

        return toResponseDTO(atualizado);
    }

    @Override
    public void deleteExpense(Long id) {

        log.info("Starting expense deletion. expenseId={}", id);

        Expense expenseExistente = findExpenseEntityById(id);
        Contract contract = expenseExistente.getContract();

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to delete an expense for a non-active contract. expenseId={} | contractId={}",
                    id, contract.getId());
            throw new BusinessException("Only expenses from active contracts can be deleted");
        }

        expenseRepository.deleteById(expenseExistente.getId());

        log.info("Expense deleted successfully. expenseId={} | contractId={}", id, contract.getId());
    }

    public Expense findExpenseEntityById(Long id) {

        log.debug("Finding expense in database. expenseId={}", id);

        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    @Override
    public ExpenseResponseDTO findExpenseById(Long id) {

        log.info("Finding expense by id. expenseId={}", id);

        return toResponseDTO(findExpenseEntityById(id));
    }

    @Override
    public List<ExpenseResponseDTO> buscarTodosexpenses() {

        log.info("Finding all registered expenses.");

        return expenseRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ExpenseResponseDTO toResponseDTO(Expense expense) {

        log.debug("Converting expense to DTO. expenseId={} | contractId={}",
                expense.getId(), expense.getContract().getId());

        ExpenseResponseDTO dto = new ExpenseResponseDTO();

        dto.setId(expense.getId());
        dto.setdependentId(expense.getContract().getDependent().getId());
        dto.setuserId(expense.getContract().getUser().getId());
        dto.setcontractId(expense.getContract().getId());
        dto.setDescription(expense.getDescription());
        dto.setValue(expense.getValue());
        dto.setData(expense.getDataExpense());
        dto.setReceiptUrl(expense.getReceiptUrl());

        return dto;
    }
}