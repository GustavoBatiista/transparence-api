package dev.java.transparence.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.java.transparence.dto.ContractRequestDTO;
import dev.java.transparence.dto.ContractResponseDTO;
import dev.java.transparence.entity.Contract;
import dev.java.transparence.entity.Dependent;
import dev.java.transparence.entity.User;
import dev.java.transparence.enums.ContractStatus;
import dev.java.transparence.exception.BusinessException;
import dev.java.transparence.exception.ResourceNotFoundException;
import dev.java.transparence.repository.ContractRepository;

@Service
public class ContractServiceImpl implements ContractService {

    private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private ContractRepository contractRepository;
    private userService userService;
    private dependentService dependentService;

    public ContractServiceImpl(ContractRepository contractRepository,
            userService userService,
            dependentService dependentService) {
        this.contractRepository = contractRepository;
        this.userService = userService;
        this.dependentService = dependentService;
    }

    @Override
    public ContractResponseDTO createContract(ContractRequestDTO dto) {

        log.info("Starting contract creation. userId={}, dependentId={}",
                dto.getuserId(), dto.getdependentId());

        User user = userService.findUserByContract(dto.getuserId());
        Dependent dependent = dependentService.findDependentEntityById(dto.getdependentId());

        if (contractRepository.existsByuser_IdAnddependent_IdAndStatus(
                user.getId(), dependent.getId(), ContractStatus.ACTIVE)) {

            log.warn("Attempt to create a contract that already exists. userId={}, dependentId={}",
                    dto.getuserId(), dto.getdependentId());

            throw new BusinessException("Contract already exists");
        }

        Contract contract = new Contract(user, dependent, LocalDate.now());
        Contract save = contractRepository.save(contract);

        log.info("Contract created successfully. contractId={} | status={}",
                save.getId(), save.getStatus());

        return toResponseDTO(save);
    }

    @Override
    public ContractResponseDTO closeContract(Long id) {

        log.info("Starting contract closing. contractId={}", id);

        Contract contractExistente = findContractForOperation(id);

        if (contractExistente.getStatus() == ContractStatus.CLOSED) {
            log.warn("Attempt to close a contract that is already closed. contractId={}", id);
            throw new BusinessException("Contract is already closed");
        }

        contractExistente.setStatus(ContractStatus.CLOSED);
        contractExistente.setendDate(LocalDate.now());

        log.info("Contract closed successfully. contractId={}", id);

        return toResponseDTO(contractRepository.save(contractExistente));
    }

    @Override
    public ContractResponseDTO suspendContract(Long id) {

        log.info("Starting contract suspension. contractId={}", id);

        Contract contractExistente = findContractForOperation(id);

        if (contractExistente.getStatus() != ContractStatus.ACTIVE) {
            log.warn("Attempt to suspend a contract that is not active. contractId={}", id);
            throw new BusinessException("Only active contracts can be suspended");
        }

        contractExistente.setStatus(ContractStatus.SUSPENDED);

        log.info("Contract suspended successfully. contractId={}", id);

        return toResponseDTO(contractRepository.save(contractExistente));
    }

    @Override
    public ContractResponseDTO reactivateContract(Long id) {

        log.info("Starting contract reactivation. contractId={}", id);

        Contract contractExistente = findContractForOperation(id);

        if (contractExistente.getStatus() != ContractStatus.SUSPENDED) {
            log.warn("Attempt to reactivate a contract that is not suspended. contractId={}", id);
            throw new BusinessException("Only suspended contracts can be reactivated");
        }

        contractExistente.setStatus(ContractStatus.ACTIVE);

        log.info("Contract reactivated successfully. contractId={}", id);

        return toResponseDTO(contractRepository.save(contractExistente));
    }

    @Override
    public void deleteContract(Long id) {

        log.info("Starting contract deletion. contractId={}", id);

        if (!contractRepository.existsById(id)) {
            log.warn("Attempt to delete a non-existing contract. contractId={}", id);
            throw new ResourceNotFoundException("Contract not found");
        }

        contractRepository.deleteById(id);

        log.info("Contract deleted successfully. contractId={}", id);
    }

    @Override
    public Contract findContractForOperation(Long id) {

        log.debug("Finding contract for operation. contractId={}", id);

        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
    }

    @Override
    public ContractResponseDTO findContractById(Long id) {

        log.info("Finding contract by id. contractId={}", id);

        return toResponseDTO(findContractForOperation(id));
    }

    private ContractResponseDTO toResponseDTO(Contract contract) {

        log.debug("Converting contract to DTO. contractId={}", contract.getId());

        ContractResponseDTO dto = new ContractResponseDTO();

        dto.setId(contract.getId());
        dto.setuserId(contract.getuser().getId());
        dto.setdependentId(contract.getdependent().getId());
        dto.setstartDate(contract.getstartDate());
        dto.setendDate(contract.getendDate());
        dto.setStatus(contract.getStatus());

        return dto;
    }
}