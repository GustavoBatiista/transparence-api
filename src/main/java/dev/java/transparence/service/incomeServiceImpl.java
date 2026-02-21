package dev.java.transparence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.IncomeRequestDTO;
import dev.java.transparence.dto.IncomeResponseDTO;
import dev.java.transparence.entity.Contract;
import dev.java.transparence.entity.Income;
import dev.java.transparence.enums.ContractStatus;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.IncomeRepository;

@Service
public class incomeServiceImpl implements incomeService {

    private static final Logger log = LoggerFactory.getLogger(incomeServiceImpl.class);

    private IncomeRepository incomeRepository;
    private ContractService contractService;

    public incomeServiceImpl(IncomeRepository incomeRepository, ContractService contractService) {
        this.incomeRepository = incomeRepository;
        this.contractService = contractService;
    }

    @Override
    public IncomeResponseDTO createIncome(IncomeRequestDTO dto) {

        log.info("Starting income creation. contractId={}", dto.getcontractId());

        Contract contract = contractService.findContractForOperation(dto.getcontractId());

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to create an income for a non-active contract. contractId={}", dto.getcontractId());
            throw new BusinessException("Only active contracts can receive incomes");
        }

        boolean existeincome = incomeRepository
                .existsBycontract_IdAndDataincomeAndvalue(
                        dto.getcontractId(),
                        dto.getDataincome(),
                        dto.getvalue());

        if (existeincome) {
            log.warn("Attempt to create an already existing income. contractId={}", dto.getcontractId());
            throw new BusinessException("Income already exists");
        }

        Income income = new Income(contract,
                dto.getDescription(),
                dto.getvalue(),
                dto.getDataincome());

        Income save = incomeRepository.save(income);

        log.info("Income created successfully. incomeId={} | contractId={}",
                save.getId(), dto.getcontractId());

        return toResponseDTO(save);
    }

    @Override
    public IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO dto) {

        Income incomeExistente = findIncomeEntityById(id);
        Contract contract = incomeExistente.getcontract();

        log.info("Starting income update. incomeId={} | contractId={}", id, contract.getId());

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to update an income for a non-active contract. contractId={} | status={}",
                    contract.getId(), contract.getStatus());
            throw new BusinessException("Only incomes from active contracts can be updated");
        }

        incomeExistente.setDescription(dto.getDescription());
        incomeExistente.setvalue(dto.getvalue());
        incomeExistente.setDataincome(dto.getDataincome());

        Income atualizado = incomeRepository.save(incomeExistente);

        log.info("Income updated successfully. incomeId={} | contractId={}",
                atualizado.getId(), atualizado.getcontract().getId());

        return toResponseDTO(atualizado);
    }

    @Override
    public void deleteIncome(Long id) {

        log.info("Starting income deletion. incomeId={}", id);

        Income incomeExistente = findIncomeEntityById(id);
        Contract contract = incomeExistente.getcontract();

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to delete an income for a non-active contract. contractId={} | status={}",
                    contract.getId(), contract.getStatus());
            throw new BusinessException("Only incomes from active contracts can be deleted");
        }

        incomeRepository.deleteById(incomeExistente.getId());

        log.info("Income deleted successfully. incomeId={} | contractId={}", id, contract.getId());
    }

    public Income findIncomeEntityById(Long id) {

        log.debug("Finding income in database. incomeId={}", id);

        return incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));
    }

    @Override
    public IncomeResponseDTO findIncomeById(Long id) {

        log.info("Finding income by id. incomeId={}", id);

        return toResponseDTO(findIncomeEntityById(id));
    }

    @Override
    public List<IncomeResponseDTO> findAllIncome() {

        log.info("Finding all registered incomes.");

        return incomeRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private IncomeResponseDTO toResponseDTO(Income income) {

        log.debug("Converting income to DTO. incomeId={} | contractId={}",
                income.getId(), income.getcontract().getId());

        IncomeResponseDTO dto = new IncomeResponseDTO();

        dto.setId(income.getId());
        dto.setdependentId(income.getcontract().getdependent().getId());
        dto.setuserId(income.getcontract().getuser().getId());
        dto.setcontractId(income.getcontract().getId());
        dto.setDescription(income.getDescription());
        dto.setvalue(income.getvalue());
        dto.setData(income.getDataincome());
        dto.setReceiptUrl(income.getReceiptUrl());

        return dto;
    }
}