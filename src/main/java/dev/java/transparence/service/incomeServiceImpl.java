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

        log.info("Starting income creation. contractId={}", dto.getContractId());

        Contract contract = contractService.findContractForOperation(dto.getContractId());

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to create an income for a non-active contract. contractId={}", dto.getContractId());
            throw new BusinessException("Only active contracts can receive incomes");
        }

        boolean existeincome = incomeRepository
                .existsByContract_IdAndDataIncomeAndValue(
                        dto.getContractId(),
                        dto.getDataIncome(),
                        dto.getValue());

        if (existeincome) {
            log.warn("Attempt to create an already existing income. contractId={}", dto.getContractId());
            throw new BusinessException("Income already exists");
        }

        Income income = new Income(contract,
                dto.getDescription(),
                dto.getValue(),
                dto.getDataIncome());

        Income save = incomeRepository.save(income);

        log.info("Income created successfully. incomeId={} | contractId={}",
                save.getId(), dto.getContractId());

        return toResponseDTO(save);
    }

    @Override
    public IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO dto) {

        Income incomeExistente = findIncomeEntityById(id);
        Contract contract = incomeExistente.getContract();

        log.info("Starting income update. incomeId={} | contractId={}", id, contract.getId());

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to update an income for a non-active contract. contractId={} | status={}",
                    contract.getId(), contract.getStatus());
            throw new BusinessException("Only incomes from active contracts can be updated");
        }

        incomeExistente.setDescription(dto.getDescription());
        incomeExistente.setValue(dto.getValue());
        incomeExistente.setDataIncome(dto.getDataIncome());

        Income atualizado = incomeRepository.save(incomeExistente);

        log.info("Income updated successfully. incomeId={} | contractId={}",
                atualizado.getId(), atualizado.getContract().getId());

        return toResponseDTO(atualizado);
    }

    @Override
    public void deleteIncome(Long id) {

        log.info("Starting income deletion. incomeId={}", id);

        Income incomeExistente = findIncomeEntityById(id);
        Contract contract = incomeExistente.getContract();

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
                income.getId(), income.getContract().getId());

        IncomeResponseDTO dto = new IncomeResponseDTO();

        dto.setId(income.getId());
        dto.setdependentId(income.getContract().getDependent().getId());
        dto.setuserId(income.getContract().getUser().getId());
        dto.setcontractId(income.getContract().getId());
        dto.setDescription(income.getDescription());
        dto.setValue(income.getValue());
        dto.setData(income.getDataIncome());
        dto.setReceiptUrl(income.getReceiptUrl());

        return dto;
    }
}